package com.example.riskFinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.riskFinder.model.Crack;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CrackRepository extends JpaRepository<Crack, Long> {

    @Query("SELECT MAX(c.detectedAt) FROM Crack c WHERE c.crackId = :crackId")
    LocalDate findLatestDetectionDate(String crackId);

    List<Crack> findByCrackId(String crackId);
}
