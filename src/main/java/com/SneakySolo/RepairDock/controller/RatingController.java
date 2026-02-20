package com.SneakySolo.RepairDock.controller;

import com.SneakySolo.RepairDock.domain.rating.Rating;
import com.SneakySolo.RepairDock.service.RatingService;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/api/secure-test")
    public String secureEndpoint(HttpSession session) {
        if (session.getAttribute("USER_ID") == null) {
            throw new RuntimeException("Not authenticated");
        }

        return "You are authenticated!";
    }

}
