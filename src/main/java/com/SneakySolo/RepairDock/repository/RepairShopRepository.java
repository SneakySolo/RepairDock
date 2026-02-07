package com.SneakySolo.RepairDock.repository;

import com.SneakySolo.RepairDock.domain.repairshop.RepairShop;
import com.SneakySolo.RepairDock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepairShopRepository extends JpaRepository<RepairShop, Long> {
    Optional<RepairShop> findByOwner(User owner);
    Optional<RepairShop> findByOwnerId(Long ownerId);
}
