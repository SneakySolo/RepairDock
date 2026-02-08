package com.SneakySolo.RepairDock.repository;

import com.SneakySolo.RepairDock.domain.request.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, String>
{
}
