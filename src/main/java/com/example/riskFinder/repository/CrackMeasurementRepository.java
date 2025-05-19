package com.example.riskFinder.repository;


import com.example.riskFinder.model.CrackMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CrackMeasurementRepository extends JpaRepository<CrackMeasurement, Long> {
    List<CrackMeasurement> findByCrackId(String crackId);
}
