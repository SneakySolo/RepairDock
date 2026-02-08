package com.SneakySolo.RepairDock.repository;

import com.SneakySolo.RepairDock.domain.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request,Long> {
}
