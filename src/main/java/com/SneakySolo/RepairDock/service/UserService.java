package com.SneakySolo.RepairDock.service;

import com.SneakySolo.RepairDock.dto.RegisterRequestDTO;
import com.SneakySolo.RepairDock.entity.User;
import com.SneakySolo.RepairDock.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerCustomer(RegisterRequestDTO registerRequestDTO) {
        return null;
    }
}
