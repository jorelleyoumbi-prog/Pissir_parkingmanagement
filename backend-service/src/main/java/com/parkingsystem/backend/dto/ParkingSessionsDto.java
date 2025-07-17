package com.parkingsystem.backend.dto;

import java.time.LocalDateTime;

/**
 * Cette classe DTO (Data Transfer Object) est utilisée pour transférer
 * les données nécessaires à la création ou la fermeture d'une session de stationnement.
 * Elle permet d'éviter d'exposer directement l'entité ParkingSession dans l'API.
 */

public class ParkingSessionsDto {

	private Long userId;  // identifiant de l'utilisateur qui entre ou sort du parking
	private Long parkingSpotId; // identifiant du poste de stationnement concerné
	private Long sessionId; // identifiant de la session qui permet dindentifier quelle session est entrain de se terminer lors de l'exit du parking
	private LocalDateTime entryTime;
	private LocalDateTime exitTime;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId= userId;
	}
	public Long getParkingSpotId() {
		return parkingSpotId;
	}
	public void setParkingSpotId(Long parkingSpotId) {
		this.parkingSpotId=parkingSpotId;
	}
	
	public Long getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(Long sessionId) {
		this.sessionId=sessionId;
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
}
