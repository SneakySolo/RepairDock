package com.SneakySolo.RepairDock.controller;

import com.SneakySolo.RepairDock.domain.rating.Rating;
import com.SneakySolo.RepairDock.service.RatingService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public Rating createRating(@RequestParam Long requestId,
                               @Valid @RequestParam Integer stars,
                               @RequestParam String comment,
                               HttpSession session) {

        return ratingService.createRating(
                requestId,
                stars,
                comment,
                session
        );
    }

    @GetMapping("/{shopId}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long shopId) {
        return ResponseEntity.ok(ratingService.getAverageRating(shopId));
    }
}
