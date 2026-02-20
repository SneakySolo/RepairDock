package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.domain.user.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public Long getCurrentUserId(HttpSession session) {
        return (Long) session.getAttribute("USER_ID");
    }

    public Role getCurrentUserRole(HttpSession session) {
        return (Role) session.getAttribute("USER_ROLE");
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("USER_ID") != null;
    }

    public void requireLogin (HttpSession session) {
        if (!isLoggedIn(session)) {
            throw new RuntimeException("You are not logged in!");
        }
    }

    public void requiredRole (HttpSession session, Role reqRole) {

        Role currentRole =  getCurrentUserRole(session);

        if (currentRole == null) {
            throw new RuntimeException("You must be logged in");
        }

        if (reqRole != null && reqRole != currentRole) {
            throw new RuntimeException("insufficient permission");
        }
    }
}
