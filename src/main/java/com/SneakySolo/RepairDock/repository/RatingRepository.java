package com.SneakySolo.RepairDock.repository;

import com.SneakySolo.RepairDock.domain.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByRequestId(Long requestId);

    @Query("SELECT AVG(r.star) FROM Rating r WHERE r.repairShop.id = :shopId")
    Double getAverageRatingByShopId(@Param("shopId") Long shopId);
}
