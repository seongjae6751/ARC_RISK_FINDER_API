package com.example.riskFinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.riskFinder.model.Crack;

public interface CrackRepository extends JpaRepository<Crack, Long> {
}
