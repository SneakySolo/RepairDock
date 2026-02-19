package com.SneakySolo.RepairDock.repository;

import com.SneakySolo.RepairDock.domain.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByRequestId(Long requestId);
}
