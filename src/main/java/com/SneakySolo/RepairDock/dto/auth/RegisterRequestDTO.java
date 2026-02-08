package com.SneakySolo.RepairDock.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record RegisterRequestDTO(
        @NotEmpty
        String fullName,

        @NotEmpty
        String phoneNumber,

        @NotEmpty
        String email,

        @NotEmpty
        @Length (min = 6)
        String password) {

}
