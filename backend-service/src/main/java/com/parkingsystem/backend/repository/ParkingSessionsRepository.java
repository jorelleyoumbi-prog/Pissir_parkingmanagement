package com.parkingsystem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parkingsystem.backend.model.ParkingSessions;

@Repository
public interface ParkingSessionsRepository extends JpaRepository<ParkingSessions,Long> {
  ParkingSessions findTopByUserIdAndExitTimeIsNull(Long userId); //permet de trouver la dernière session en cours(non encore terminée) pour un utilisateur donné
  ParkingSessions findTopByUserIdAndParkingSpotIdAndExitTimeIsNotNullOrderByExitTimeDesc(Long userId, Long parkingSpotId);
} 
