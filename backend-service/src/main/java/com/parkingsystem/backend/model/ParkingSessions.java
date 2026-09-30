package com.parkingsystem.backend.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="parking_sessions")

public class ParkingSessions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id",nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "parking_spot_id",nullable = false)
	private ParkingSpot parkingSpot;
	
	private LocalDateTime entryTime;
	private LocalDateTime exitTime;
	
	private Double parkingCosto;
	private Double chargingCosto;
	private Double totalCosto;
	
	//association à unr requete de recharge (via mwbot)
	// Correct :
	@OneToMany(mappedBy = "parkingSessions", cascade = CascadeType.ALL)
	private List<ChargingRequest> chargingRequests;
	
	@OneToOne(mappedBy = "parkingSessions", cascade = CascadeType.ALL)
	private Payment payment;
	
	public Long getId() {
		return id;
	}
	public void  setId(Long id) {
		this.id= id;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user=user;
	}
	
	public  ParkingSpot getParkingSpot() {
		return parkingSpot;
	}
	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot=parkingSpot;
	}
	
	public LocalDateTime getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(LocalDateTime entryTime) {
		this.entryTime=entryTime;
	}
	
	public LocalDateTime getExitTime() {
		return exitTime;
	}
	public void setExitTime(LocalDateTime exitTime) {
		this.exitTime=exitTime;
	}
	
	public Double getParkingCosto() {
		return parkingCosto;
	}
	
	public void setParkingCosto(Double parkingCosto) {
		this.parkingCosto=parkingCosto;
	}
	
	public Double getChargingCosto() {
		return chargingCosto;
	}
	
	public void setChargingCosto(Double chargingCosto) {
		this.chargingCosto=chargingCosto;
	}
	
	public Double getTotalCosto() {
		return totalCosto;
	}
	
	public void setTotalCosto(Double totalCosto) {
		this.totalCosto=totalCosto;
	}
	
	public List<ChargingRequest> getChargingRequests(){
		return chargingRequests;
	}
	
	public void setChargingRequests(List<ChargingRequest> chargingRequests) {
		this.chargingRequests=chargingRequests;
	}
	
	public Payment getPayment() {
		return payment;
	}
	
	public void setPayment(Payment payment) {
		this.payment= payment;
	}
}
