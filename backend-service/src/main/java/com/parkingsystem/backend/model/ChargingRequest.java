package com.parkingsystem.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "charging_requests")
public class ChargingRequest {

    public enum ChargingStatus {
        PENDING, QUEUED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "mwbot_id")
    private MWbot mwbot;

    @ManyToOne
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChargingStatus status;

    @Column(nullable = false)
    private Integer initialPercentage;

    @Column(nullable = false)
    private Integer targetPercentage;

    private Integer currentPercentage;

    private LocalDateTime estimatedStartTime;
    private LocalDateTime chargingStartTime;
    private LocalDateTime estimatedCompletionTime;
    private LocalDateTime chargingCompletionTime;

    private Integer queuePosition;
    private Double totalEnergyKwh;

    @Column(nullable = false)
    private boolean notificationRequested;

    private boolean notificationSent;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ChargingRequest() {
        this.createdAt = LocalDateTime.now();
        this.currentPercentage = 0;
        this.notificationSent = false;
    }

    // --- GETTER & SETTER ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MWbot getMwbot() {
        return mwbot;
    }

    public void setMwbot(MWbot mwbot) {
        this.mwbot = mwbot;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public ChargingStatus getStatus() {
        return status;
    }

    public void setStatus(ChargingStatus status) {
        this.status = status;
    }

    public Integer getInitialPercentage() {
        return initialPercentage;
    }

    public void setInitialPercentage(Integer initialPercentage) {
        this.initialPercentage = initialPercentage;
    }

    public Integer getTargetPercentage() {
        return targetPercentage;
    }

    public void setTargetPercentage(Integer targetPercentage) {
        this.targetPercentage = targetPercentage;
    }

    public Integer getCurrentPercentage() {
        return currentPercentage;
    }

    public void setCurrentPercentage(Integer currentPercentage) {
        this.currentPercentage = currentPercentage;
    }

    public LocalDateTime getEstimatedStartTime() {
        return estimatedStartTime;
    }

    public void setEstimatedStartTime(LocalDateTime estimatedStartTime) {
        this.estimatedStartTime = estimatedStartTime;
    }

    public LocalDateTime getChargingStartTime() {
        return chargingStartTime;
    }

    public void setChargingStartTime(LocalDateTime chargingStartTime) {
        this.chargingStartTime = chargingStartTime;
    }

    public LocalDateTime getEstimatedCompletionTime() {
        return estimatedCompletionTime;
    }

    public void setEstimatedCompletionTime(LocalDateTime estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    public LocalDateTime getChargingCompletionTime() {
        return chargingCompletionTime;
    }

    public void setChargingCompletionTime(LocalDateTime chargingCompletionTime) {
        this.chargingCompletionTime = chargingCompletionTime;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }

    public Double getTotalEnergyKwh() {
        return totalEnergyKwh;
    }

    public void setTotalEnergyKwh(Double totalEnergyKwh) {
        this.totalEnergyKwh = totalEnergyKwh;
    }

    public boolean isNotificationRequested() {
        return notificationRequested;
    }

    public void setNotificationRequested(boolean notificationRequested) {
        this.notificationRequested = notificationRequested;
    }

    public boolean isNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
