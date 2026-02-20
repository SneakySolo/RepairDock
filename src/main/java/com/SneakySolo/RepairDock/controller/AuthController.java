package com.SneakySolo.RepairDock.controller;

import com.SneakySolo.RepairDock.dto.auth.LoginRequestDTO;
import com.SneakySolo.RepairDock.dto.auth.LoginResponseDTO;
import com.SneakySolo.RepairDock.dto.auth.RegisterRequestDTO;
import com.SneakySolo.RepairDock.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

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
            @Valid @RequestBody LoginRequestDTO requestDTO,
            HttpSession session
    ) {
        LoginResponseDTO response = userService.loginCustomer(requestDTO);

        session.setAttribute("USER_ID", response.id());
        session.setAttribute("USER_ROLE", response.role());
        session.setAttribute("USER_NAME", response.fullname());

        return response;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Logged out successfully";
    }

    @GetMapping("/api/secure-test")
    public String secureEndpoint(HttpSession session) {
        if (session.getAttribute("USER_ID") == null) {
            throw new RuntimeException("Not authenticated");
        }

        return "You are authenticated!";
    }

}
