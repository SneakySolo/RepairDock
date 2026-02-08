package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.dto.auth.LoginRequestDTO;
import com.SneakySolo.RepairDock.dto.auth.LoginResponseDTO;
import com.SneakySolo.RepairDock.dto.auth.RegisterRequestDTO;
import com.SneakySolo.RepairDock.domain.user.Role;
import com.SneakySolo.RepairDock.domain.user.User;
import com.SneakySolo.RepairDock.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerCustomer(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email()) ||
            userRepository.existsByPhoneNumber(dto.phoneNumber())
        ) {
            throw new IllegalStateException("Email or phone number already exists");
        }

        User customer = new User();
        customer.setFullName(dto.fullName());
        customer.setEmail(dto.email());
        customer.setPhoneNumber(dto.phoneNumber());
        customer.setRole(Role.CUSTOMER);

        customer.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(customer);
    }

    public LoginResponseDTO loginCustomer(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("User is disabled");
        }

        return new LoginResponseDTO(
                user.getFullName(),
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}
