package com.example.riskFinder.repository;

import java.util.List;

import com.example.riskFinder.model.Waypoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaypointRepository extends JpaRepository<Waypoint, Long> {
    List<Waypoint> findByBuildingId(Long buildingId);
}
