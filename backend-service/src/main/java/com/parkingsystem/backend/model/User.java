package com.parkingsystem.backend.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
=======
import com.fasterxml.jackson.annotation.JsonIgnore;

>>>>>>> 3204814 (aggiunta della parking sessions)

/**
 * Entity representing a user of the system
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    // User role: BASE_USER, PREMIUM_USER, ADMIN
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // For premium users, storing payment information
    @Column(name = "credit_card_number")
    private String creditCardNumber;

    @Column(name = "credit_card_expiry")
    private String creditCardExpiry;

    @Column(name = "credit_card_cvv")
    private String creditCardCvv;

    // Car information
    @Column(name = "car_model")
    private String carModel;

    @Column(name = "car_license_plate")
    private String carLicensePlate;

    @Column(name = "battery_capacity_kw")
    private Double batteryCapacityKw;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Reservation> reservations = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<ChargingRequest> chargingRequests = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
<<<<<<< HEAD
=======
    @JsonIgnore
>>>>>>> 3204814 (aggiunta della parking sessions)
    private Set<Payment> payments = new HashSet<>();
    
    
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getCreditCardExpiry() {
		return creditCardExpiry;
	}

	public void setCreditCardExpiry(String creditCardExpiry) {
		this.creditCardExpiry = creditCardExpiry;
	}

	public String getCreditCardCvv() {
		return creditCardCvv;
	}

	public void setCreditCardCvv(String creditCardCvv) {
		this.creditCardCvv = creditCardCvv;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getCarLicensePlate() {
		return carLicensePlate;
	}

	public void setCarLicensePlate(String carLicensePlate) {
		this.carLicensePlate = carLicensePlate;
	}

	public Double getBatteryCapacityKw() {
		return batteryCapacityKw;
	}

	public void setBatteryCapacityKw(Double batteryCapacityKw) {
		this.batteryCapacityKw = batteryCapacityKw;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Set<ChargingRequest> getChargingRequests() {
		return chargingRequests;
	}

	public void setChargingRequests(Set<ChargingRequest> chargingRequests) {
		this.chargingRequests = chargingRequests;
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	@PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public boolean isPremium() {
        return role == UserRole.PREMIUM_USER || role == UserRole.ADMIN;
    }
    
    public enum UserRole {
        BASE_USER,
        PREMIUM_USER,
        ADMIN
    }
}
