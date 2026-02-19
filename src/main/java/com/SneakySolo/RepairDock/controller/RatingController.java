package com.SneakySolo.RepairDock.controller;

import com.SneakySolo.RepairDock.domain.rating.Rating;
import com.SneakySolo.RepairDock.service.RatingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public Rating createRating(@RequestParam Long requestId,
                               @RequestParam Integer stars,
                               @RequestParam String comment,
                               @RequestParam Long customerId) {

        return ratingService.createRating(
                requestId,
                stars,
                comment,
                customerId
        );
    }
}
