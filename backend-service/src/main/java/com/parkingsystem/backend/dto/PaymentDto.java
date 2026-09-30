package com.parkingsystem.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Payment entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    
    private Long id;
    
    private Long userId;
    
    private String username;
<<<<<<< HEAD
=======
    private Long parkingSessionsId;  // identifiant de la session de parking associée au payment
>>>>>>> 3204814 (aggiunta della parking sessions)
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}
<<<<<<< HEAD
=======
	
	public Long getParkingSessionsId() {
		return parkingSessionsId;
	}
	public void setParkingSessionsId(Long parkingSessionsId) {
		this.parkingSessionsId= parkingSessionsId;
	} //cela permet de transférer l'identifiant de la session de stationnement associée au paiement
>>>>>>> 3204814 (aggiunta della parking sessions)

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public Long getChargingRequestId() {
		return chargingRequestId;
	}

	public void setChargingRequestId(Long chargingRequestId) {
		this.chargingRequestId = chargingRequestId;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCardLastFour() {
		return cardLastFour;
	}

	public void setCardLastFour(String cardLastFour) {
		this.cardLastFour = cardLastFour;
	}

	private Long reservationId;
    
    private Long chargingRequestId;
    
    private String paymentDate;
    
    private Double amount;
    
    private String currency;
    
    private String type;
    
    private String status;
    
    private String transactionId;
    
    private String paymentMethod;
    
    private String cardLastFour;
<<<<<<< HEAD
=======
    

>>>>>>> 3204814 (aggiunta della parking sessions)
}