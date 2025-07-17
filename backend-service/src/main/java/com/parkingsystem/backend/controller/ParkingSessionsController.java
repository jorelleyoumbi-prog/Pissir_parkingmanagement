package com.parkingsystem.backend.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.parkingsystem.backend.dto.ParkingSessionsDto;
import com.parkingsystem.backend.model.ChargingRequest;
import com.parkingsystem.backend.model.ParkingSessions;
import com.parkingsystem.backend.model.ParkingSpot;
import com.parkingsystem.backend.model.Payment;
import com.parkingsystem.backend.model.Payment.PaymentStatus;
import com.parkingsystem.backend.model.Payment.PaymentType;
import com.parkingsystem.backend.model.User;
import com.parkingsystem.backend.repository.ChargingRequestRepository;
import com.parkingsystem.backend.repository.ParkingSessionsRepository;
import com.parkingsystem.backend.repository.ParkingSpotRepository;
import com.parkingsystem.backend.repository.PaymentRepository;
import com.parkingsystem.backend.repository.UserRepository;

import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/parking-sessions")
public class ParkingSessionsController {

	 @Autowired
	 private ParkingSessionsRepository sessionRepository;
	 
	 @Autowired
	 private UserRepository userRepository;
	 
	 @Autowired
	 private ParkingSpotRepository parkingSpotRepository;
	 
	 @Autowired
	 private PaymentRepository paymentRepository;
	 @Autowired
	 private ChargingRequestRepository chargingRequestRepository;

	 
	 private static final double PARKING_RATE_PER_HOUR=2.50;
	 private static final double CHARGING_RATE_PER_KW=0.45;
	 
	 @PostMapping("/entry")
	 public ResponseEntity<ParkingSessions>startSession(@RequestBody ParkingSessionsDto dto){
		
		 //System.out.println("➡️ userId reçu = " + dto.getUserId());
		 //System.out.println("➡️ parkingSpotId reçu = " + dto.getParkingSpotId());
		 
		 if (dto.getUserId() == null || dto.getParkingSpotId() == null) {
			    throw new IllegalArgumentException("userId and parkingSpotId must not be null");
			}
		 
		 User user = userRepository.findById(dto.getUserId())
				    .orElseThrow(() -> new IllegalArgumentException("User not found: id = " + dto.getUserId()));
		 ParkingSpot spot = parkingSpotRepository.findById(dto.getParkingSpotId())
				    .orElseThrow(() -> new IllegalArgumentException("Parking spot not found: id = " + dto.getParkingSpotId()));

		 
		 ParkingSessions session = new ParkingSessions();
		 session.setUser(user);
		 session.setParkingSpot(spot);
		 session.setEntryTime(dto.getEntryTime());
		 
		 return ResponseEntity.ok(sessionRepository.save(session));
	 }
	  
	 @PostMapping("/exit")
	 public ResponseEntity<ParkingSessions>endSession(@RequestBody ParkingSessionsDto dto){
		 ParkingSessions session = sessionRepository.findTopByUserIdAndExitTimeIsNull(dto.getUserId());
         if(session==null) return ResponseEntity.notFound()	.build();
         
         /*ParkingSessions session = sessionRepository.findById(dto.getSessionId())
        	        .orElse(null);

        	    if (session == null || session.getExitTime() != null) {
        	        return ResponseEntity.notFound().build(); // session inexistante ou déjà clôturée
        	    }*/
         
         LocalDateTime exitTime=dto.getExitTime();
         session.setExitTime(exitTime);
         
         List<ChargingRequest> orphanRequests = chargingRequestRepository
                 .findByUserIdAndParkingSessionsIsNull(dto.getUserId());
         for (ChargingRequest r : orphanRequests) {
             r.setParkingSessions(session);
         }
         chargingRequestRepository.saveAll(orphanRequests);
         
         //calcul de cout du temos de stationnement 
         long minutes= Duration.between(session.getEntryTime(), exitTime).toMinutes();
         double hours =minutes/60.0;
         double parkingCosto=hours*PARKING_RATE_PER_HOUR;
         session.setParkingCosto(parkingCosto);
         
         //calcul du cout de la recharge
         
         double totalEnergyDelivered=0.0;
         List<ChargingRequest> requests=session.getChargingRequests();
         if(requests!=null) {
        	 for (ChargingRequest r:requests) {
        		 totalEnergyDelivered += r.getTotalEnergyKwh();
        	 }
         }
         
         double chargingCosto =totalEnergyDelivered*CHARGING_RATE_PER_KW;
         session.setChargingCosto(chargingCosto);
         //calcul du cout total
        double totalCosto= parkingCosto+chargingCosto;
         session.setTotalCosto(totalCosto);
         
         //paiemment lié à la session
         
         Payment payment=new Payment();
         payment.setAmount(totalCosto);
         payment.setCurrency("EUR");
         payment.setPaymentMethod("CREDIT_CARD");
         payment.setStatus(PaymentStatus.PENDING);
         payment.setPaymentDate(exitTime);
         payment.setUser(session.getUser());
         payment.setParkingSessions(session);
         
         payment.setTransactionId("TXN-" + System.currentTimeMillis()); // génère un ID unique
         PaymentType paymentType = (chargingCosto > 0)
        		    ? PaymentType.PARKING_AND_CHARGING
        		    : PaymentType.PARKING_ONLY;
         payment.setType(paymentType);
         payment.setCardLastFour("1234"); 

          paymentRepository.save(payment);
          session.setPayment(payment);
         return ResponseEntity.ok(sessionRepository.save(session));
	 }
	 
	

	 
}
