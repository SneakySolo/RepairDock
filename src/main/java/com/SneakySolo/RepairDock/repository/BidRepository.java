package com.SneakySolo.RepairDock.repository;

import com.SneakySolo.RepairDock.domain.bid.Bid;
import com.SneakySolo.RepairDock.domain.bid.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid,Long> {
    List <Bid> findByRequestId(Long reqId);
    Optional<Bid> findByRequestIdAndRepairShopId(Long requestId, Long repairShopId);
    boolean existsByRequestIdAndRepairShopId(Long reqId, Long repId);
    Optional<Bid> findByRequestIdAndStatus(Long requestId, BidStatus status);
}
