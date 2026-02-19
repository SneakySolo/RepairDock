package com.SneakySolo.RepairDock.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public Long getCurrentUserId(HttpSession session) {
        return (Long) session.getAttribute("USER_ID");
    }

    public String getCurrentUserRole(HttpSession session) {
        return (String) session.getAttribute("USER_ROLE");
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("USER_ID") != null;
    }
}
