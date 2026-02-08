package com.SneakySolo.RepairDock.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RequestCreateDTO(
        @NotBlank
        String Title,

        @NotBlank
        String Description,

        @NotEmpty
        List<MediaCreateDTO> mediaList
) {
}
