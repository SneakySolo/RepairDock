package com.SneakySolo.RepairDock.dto;

import com.SneakySolo.RepairDock.entity.Role;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public record LoginResponseDTO(
        String fullname,
        Long id,
        String email,
        Role role
) {
}
