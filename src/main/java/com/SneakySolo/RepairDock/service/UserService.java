package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.config.SecurityBeansConfig;
import com.SneakySolo.RepairDock.dto.RegisterRequestDTO;
import com.SneakySolo.RepairDock.entity.Role;
import com.SneakySolo.RepairDock.entity.User;
import com.SneakySolo.RepairDock.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
}
