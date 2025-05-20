package com.parkingsystem.backend.dto;

import java.time.LocalDateTime;

/**
 * DTO for reservation
 */
public class ReservationDto {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private boolean chargingRequired;
    private Long parkingSpotId;
    private String parkingSpotNumber;
    
    // Getters and setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isChargingRequired() {
        return chargingRequired;
    }
    
    public void setChargingRequired(boolean chargingRequired) {
        this.chargingRequired = chargingRequired;
    }
    
    public Long getParkingSpotId() {
        return parkingSpotId;
    }
    
    public void setParkingSpotId(Long parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }
    
    public String getParkingSpotNumber() {
        return parkingSpotNumber;
    }
    
    public void setParkingSpotNumber(String parkingSpotNumber) {
        this.parkingSpotNumber = parkingSpotNumber;
    }
}