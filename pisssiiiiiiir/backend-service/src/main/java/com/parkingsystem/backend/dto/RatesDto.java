package com.parkingsystem.backend.dto;

/**
 * DTO for parking and charging rates
 */
public class RatesDto {

    private Double parkingHourlyRate;
    private Double chargingRatePerKwh;

    // Getters and Setters
    public Double getParkingHourlyRate() {
        return parkingHourlyRate;
    }

    public void setParkingHourlyRate(Double parkingHourlyRate) {
        this.parkingHourlyRate = parkingHourlyRate;
    }

    public Double getChargingRatePerKwh() {
        return chargingRatePerKwh;
    }

    public void setChargingRatePerKwh(Double chargingRatePerKwh) {
        this.chargingRatePerKwh = chargingRatePerKwh;
    }
}