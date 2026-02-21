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

    public List<Request> getMyRequests(HttpSession session) {
        sessionService.requiredRole(session, Role.CUSTOMER);
        Long userId = sessionService.getCurrentUserId(session);
        return requestRepository.findByCustomerId(userId);
    }

    public List<Request> getRequestsForMyShop(HttpSession session) {

        sessionService.requiredRole(session, Role.REPAIR_PERSON);

        Long userId = sessionService.getCurrentUserId(session);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RepairShop shop = repairShopRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new RuntimeException("Repair shop not found"));

        return requestRepository.findByRepairShopId(shop.getId());
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

    public List<Request> getOpenRequests() {
        return requestRepository.findByRequestStatus(RequestStatus.OPEN);
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
}
