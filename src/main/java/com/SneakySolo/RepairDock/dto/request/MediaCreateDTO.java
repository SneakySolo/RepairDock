package com.SneakySolo.RepairDock.dto.request;

import com.SneakySolo.RepairDock.domain.request.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MediaCreateDTO(
        @NotNull
        MediaType mediaType,

        @NotBlank
        String filePath
) {
}
