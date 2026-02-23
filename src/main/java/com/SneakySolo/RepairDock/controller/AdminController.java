package com.SneakySolo.RepairDock.controller;

import com.SneakySolo.RepairDock.domain.user.Role;
import com.SneakySolo.RepairDock.service.AdminService;
import com.SneakySolo.RepairDock.service.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final AdminService adminService;
    private final SessionService sessionService;

    public AdminController(AdminService adminService, SessionService sessionService) {
        this.adminService = adminService;
        this.sessionService = sessionService;
    }

    @PostMapping("/users/{id}/disable")
    public ResponseEntity<Void> disableUser (@PathVariable Long id, HttpSession session) {

        sessionService.requiredRole(session, Role.ADMIN);

        adminService.diableUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shop/{id}/disable")
    public ResponseEntity<Void> disableRepairShop (@PathVariable Long id, HttpSession session) {

        sessionService.requiredRole(session, Role.ADMIN);

        adminService.diableRepaieShop(id);
        return  ResponseEntity.ok().build();
    }
}
