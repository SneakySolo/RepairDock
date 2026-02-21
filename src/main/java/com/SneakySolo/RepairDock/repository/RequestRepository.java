package com.SneakySolo.RepairDock.repository;

import com.SneakySolo.RepairDock.domain.request.Request;
import com.SneakySolo.RepairDock.domain.request.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findByCustomerId(Long customerId);
    List<Request> findByRepairShopId(Long repairShopId);
    List<Request> findByRequestStatus(RequestStatus status);
    Optional<Request> findByIdAndRequestStatus(Long id, RequestStatus status);
}
