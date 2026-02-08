package com.SneakySolo.RepairDock.dto.auth;

import com.SneakySolo.RepairDock.domain.user.Role;

public record LoginResponseDTO(
        String fullname,
        Long id,
        String email,
        Role role
) {
}
