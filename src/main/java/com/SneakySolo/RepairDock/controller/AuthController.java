package com.SneakySolo.RepairDock.controller;

import com.SneakySolo.RepairDock.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
}
