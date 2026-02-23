package com.SneakySolo.RepairDock.repository;

import com.SneakySolo.RepairDock.domain.request.Request;
import com.SneakySolo.RepairDock.domain.request.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request,Long> {
    Page<Request> findByCustomerId(Long customerId, Pageable pageable);
    Page<Request> findByRepairShopId(Long repairShopId, Pageable pageable);
    Page<Request> findByRequestStatus(RequestStatus status, Pageable pageable);
    Optional<Request> findByIdAndRequestStatus(Long id, RequestStatus status);
}
