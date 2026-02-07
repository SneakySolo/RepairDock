package com.SneakySolo.RepairDock.controller;

import com.SneakySolo.RepairDock.dto.LoginRequestDTO;
import com.SneakySolo.RepairDock.dto.LoginResponseDTO;
import com.SneakySolo.RepairDock.dto.RegisterRequestDTO;
import com.SneakySolo.RepairDock.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/register")
    public String post (@Valid @RequestBody RegisterRequestDTO requestDTO) {
        userService.registerCustomer(requestDTO);
        return "new user added successfully";
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO requestDTO) {
        return userService.loginCustomer(requestDTO);
    }
}
