package com.SneakySolo.RepairDock.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record RequestCreateDTO(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Latitude is required")
        @Min(value = -90, message = "Latitude must be >= -90")
        @Max(value = 90, message = "Latitude must be <= 90")
        Double latitude,

        @NotNull(message = "Longitude is required")
        @Min(value = -180, message = "Longitude must be >= -180")
        @Max(value = 180, message = "Longitude must be <= 180")
        Double longitude,

        @NotEmpty(message = "At least one media item is required")
        List<MediaCreateDTO> mediaList

) {}
