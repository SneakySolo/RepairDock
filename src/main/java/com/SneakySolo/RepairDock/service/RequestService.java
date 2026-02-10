package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.domain.request.Media;
import com.SneakySolo.RepairDock.domain.request.Request;
import com.SneakySolo.RepairDock.domain.request.RequestStatus;
import com.SneakySolo.RepairDock.domain.user.Role;
import com.SneakySolo.RepairDock.domain.user.User;
import com.SneakySolo.RepairDock.dto.request.MediaCreateDTO;
import com.SneakySolo.RepairDock.dto.request.RequestCreateDTO;
import com.SneakySolo.RepairDock.repository.MediaRepository;
import com.SneakySolo.RepairDock.repository.RequestRepository;
import com.SneakySolo.RepairDock.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final RequestRepository requestRepository;

    public RequestService(UserRepository userRepository, MediaRepository mediaRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        this.requestRepository = requestRepository;
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
        req.setTitle(dto.Title());
        req.setDescription(dto.Description());
        req.setRequestStatus(RequestStatus.OPEN);

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


}
