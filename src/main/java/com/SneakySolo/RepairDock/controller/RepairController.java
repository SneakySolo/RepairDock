package com.SneakySolo.RepairDock.controller;

import com.SneakySolo.RepairDock.domain.request.Request;
import com.SneakySolo.RepairDock.domain.request.RequestStatus;
import com.SneakySolo.RepairDock.domain.user.Role;
import com.SneakySolo.RepairDock.dto.request.RequestCreateDTO;
import com.SneakySolo.RepairDock.service.RequestService;
import com.SneakySolo.RepairDock.service.SessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repairs")
public class RepairController {

    private final RequestService  requestService;
    private final SessionService sessionService;

    public RepairController(RequestService requestService, SessionService sessionService) {
        this.requestService = requestService;
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<Request> createRequest (
            @Valid @RequestBody RequestCreateDTO  dto,
            HttpSession  session ) {

        sessionService.requiredRole(session, Role.CUSTOMER);
        Long userId = sessionService.getCurrentUserId(session);

        Request createRequest = requestService.createRequest(userId, dto);
        return ResponseEntity.ok(createRequest);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<Request>> getMyRequests(
            HttpSession session,
            Pageable pageable) {
        return ResponseEntity.ok(requestService.getMyRequests(session, pageable));
    }

    @GetMapping("/repairshop/my-requests")
    public ResponseEntity<Page<Request>> getMyShopRequests(
            HttpSession session,
            Pageable pageable) {
        return ResponseEntity.ok(requestService.getRequestsForMyShop(session, pageable));
    }

    @PostMapping("/{id}/update-status")
    public ResponseEntity<Request> updateStatus(@PathVariable Long id,
                                                @RequestParam RequestStatus status,
                                                HttpSession session) {
        return ResponseEntity.ok(
                requestService.updateRequestStatus(id, status, session)
        );
    }

    @GetMapping("/open")
    public ResponseEntity<Page<Request>> getOpenRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpSession session) {

        return ResponseEntity.ok(
                requestService.getOpenRequests(session, page, size)
        );
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Request> acceptRequest(@PathVariable Long id,
                                                 HttpSession session) {
        return ResponseEntity.ok(
                requestService.acceptRequest(id, session)
        );
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Request> rejectRequest(@PathVariable Long id,
                                                 HttpSession session) {
        return ResponseEntity.ok(
                requestService.rejectRequest(id, session)
        );
    }
}
