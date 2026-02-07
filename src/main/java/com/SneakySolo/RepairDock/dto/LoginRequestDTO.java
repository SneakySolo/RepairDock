package com.SneakySolo.RepairDock.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
        @NotEmpty
        @Email
        String email,

        @NotEmpty
        String password
) {
}
