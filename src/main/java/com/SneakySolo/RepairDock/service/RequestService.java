package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.domain.repairshop.RepairShop;
import com.SneakySolo.RepairDock.domain.request.Media;
import com.SneakySolo.RepairDock.domain.request.Request;
import com.SneakySolo.RepairDock.domain.request.RequestStatus;
import com.SneakySolo.RepairDock.domain.user.Role;
import com.SneakySolo.RepairDock.domain.user.User;
import com.SneakySolo.RepairDock.dto.request.MediaCreateDTO;
import com.SneakySolo.RepairDock.dto.request.RequestCreateDTO;
import com.SneakySolo.RepairDock.repository.MediaRepository;
import com.SneakySolo.RepairDock.repository.RepairShopRepository;
import com.SneakySolo.RepairDock.repository.RequestRepository;
import com.SneakySolo.RepairDock.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final RequestRepository requestRepository;
    private final RepairShopRepository repairShopRepository;
    private final SessionService sessionService;

    public RequestService(UserRepository userRepository, MediaRepository mediaRepository, RequestRepository requestRepository, RepairShopRepository repairShopRepository, SessionService sessionService) {
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        this.requestRepository = requestRepository;
        this.repairShopRepository = repairShopRepository;
        this.sessionService = sessionService;
    }

    public Request createRequest (Long id, RequestCreateDTO dto) {
        User customer = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!customer.isEnabled()) {
            throw new RuntimeException("User is disabled");
        }

        if (customer.getRole() != Role.CUSTOMER) {
            throw new RuntimeException("User is not a customer");
        }

        Request req = new  Request();
        req.setCustomer(customer);
        req.setTitle(dto.title());
        req.setDescription(dto.description());
        req.setLatitude(dto.latitude());
        req.setLongitude(dto.longitude());
        req.setRequestStatus(RequestStatus.OPEN);
        req.setRepairShop(null);

        Request savedRequest = requestRepository.save(req);

        for (MediaCreateDTO mediaDto : dto.mediaList()) {
            Media media = new Media();
            media.setRequest(savedRequest);
            media.setMediaType(mediaDto.mediaType());
            media.setFilePath(mediaDto.filePath());

            mediaRepository.save(media);
        }
        return savedRequest;
    }

    public Page<Request> getMyRequests(HttpSession session, Pageable pageable) {
        sessionService.requiredRole(session, Role.CUSTOMER);
        Long userId = sessionService.getCurrentUserId(session);
        return requestRepository.findByCustomerId(userId, pageable);
    }

    public Page<Request> getRequestsForMyShop(HttpSession session, Pageable pageable) {
        sessionService.requiredRole(session, Role.REPAIR_PERSON);
        Long userId = sessionService.getCurrentUserId(session);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RepairShop shop = repairShopRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new RuntimeException("Repair shop not found"));

        return requestRepository.findByRepairShopId(shop.getId(), pageable);
    }

    public Request updateRequestStatus(Long requestId,
                                       RequestStatus status,
                                       HttpSession session) {

        sessionService.requiredRole(session, Role.REPAIR_PERSON);
        Long userId = sessionService.getCurrentUserId(session);

        RepairShop shop = repairShopRepository.findByOwnerId(userId)
                .orElseThrow(() -> new RuntimeException("Repair shop not found"));

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getRepairShop() == null ||
                !request.getRepairShop().getId().equals(shop.getId())) {
            throw new RuntimeException("You are not assigned to this request");
        }

        if (request.getRequestStatus() != RequestStatus.ACCEPTED ||
                status != RequestStatus.CLOSED) {
            throw new RuntimeException("Invalid status transition");
        }

        request.setRequestStatus(RequestStatus.CLOSED);

        return requestRepository.save(request);
    }

    public Page<Request> getOpenRequests(HttpSession session, int page, int size) {

        sessionService.requiredRole(session, Role.REPAIR_PERSON);
        Long userId = sessionService.getCurrentUserId(session);

        RepairShop shop = repairShopRepository.findByOwnerId(userId)
                .orElseThrow(() -> new RuntimeException("Repair shop not found"));

        if (shop.getAddress() == null) {
            throw new RuntimeException("Shop address not set");
        }

        double shopLat = shop.getAddress().getLatitude();
        double shopLon = shop.getAddress().getLongitude();

        if (shopLat == 0.0 && shopLon == 0.0) {
            throw new RuntimeException("Shop location not configured");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Request> openRequestsPage =
                requestRepository.findByRequestStatus(RequestStatus.OPEN, pageable);

        double radiusKm = 10.0;

        List<Request> filteredRequests = openRequestsPage.getContent()
                .stream()
                .filter(request -> {

                    double distance = calculateDistance(
                            shopLat,
                            shopLon,
                            request.getLatitude(),
                            request.getLongitude()
                    );

                    return distance <= radiusKm;
                })
                .toList();

        return new PageImpl<>(
                filteredRequests,
                pageable,
                openRequestsPage.getTotalElements()
        );
    }

    public Request acceptRequest(Long requestId, HttpSession session) {

        sessionService.requiredRole(session, Role.REPAIR_PERSON);
        Long userId = sessionService.getCurrentUserId(session);

        RepairShop shop = repairShopRepository.findByOwnerId(userId)
                .orElseThrow(() -> new RuntimeException("Repair shop not found"));

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getRequestStatus() != RequestStatus.OPEN) {
            throw new RuntimeException("Only OPEN requests can be accepted");
        }

        request.setRepairShop(shop);
        request.setRequestStatus(RequestStatus.ACCEPTED);

        return requestRepository.save(request);
    }

    public Request rejectRequest(Long requestId, HttpSession session) {

        sessionService.requiredRole(session, Role.REPAIR_PERSON);

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getRequestStatus() != RequestStatus.OPEN) {
            throw new RuntimeException("Only OPEN requests can be rejected");
        }

        request.setRequestStatus(RequestStatus.REJECTED);

        return requestRepository.save(request);
    }

    private double calculateDistance(double lat1, double lon1,
                                     double lat2, double lon2) {

        final int EARTH_RADIUS = 6371; // KM

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}