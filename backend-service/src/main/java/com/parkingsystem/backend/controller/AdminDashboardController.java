package com.parkingsystem.backend.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkingsystem.backend.model.MWbot;
import com.parkingsystem.backend.model.ParkingSpot;
import com.parkingsystem.backend.service.MWbotService;
import com.parkingsystem.backend.service.ParkingService;
import com.parkingsystem.backend.service.PaymentService;
import com.parkingsystem.backend.service.ReservationService;
import com.parkingsystem.backend.service.UserService;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MWbotService mwbotService;

    /**
     * Endpoint per ottenere il riepilogo della dashboard admin.
     * Restituisce statistiche su:
     * - Numero totale di utenti e premium
     * - Stato dei posti auto (totale, occupati, percentuale di occupazione)
     * - Stato dei MWbot (totale, disponibili, occupati, in manutenzione)
     * - Revenue di oggi
     */
    @GetMapping("/summary")
    public ResponseEntity<?> getDashboardSummary() {
        try {
            // Utenti
            int totalUsers = userService.getAllUsers().size();
            int premiumUsers = userService.getAllPremiumUsers().size();

            // Prenotazioni
            int totalReservations = reservationService.getAllReservations().size();

            // Parcheggi
            List<ParkingSpot> allSpots = parkingService.getAllParkingSpots();
            int totalSpots = allSpots.size();
            int occupiedSpots = (int) allSpots.stream().filter(ParkingSpot::isOccupied).count();
            double occupancyRate = totalSpots > 0 ? (double) occupiedSpots / totalSpots * 100 : 0;

            // MWbot
            List<MWbot> allMwbots = mwbotService.getAllMWbots();
            int totalMwbots = allMwbots.size();
            int availableMwbots = (int) allMwbots.stream().filter(bot -> bot.getStatus() == MWbot.BotStatus.AVAILABLE).count();
            int busyMwbots = (int) allMwbots.stream().filter(bot -> bot.getStatus() == MWbot.BotStatus.BUSY).count();
            int maintenanceMwbots = (int) allMwbots.stream().filter(MWbot::isMaintenanceRequired).count();

            // Revenue di oggi: assumiamo che PaymentService abbia un metodo getRevenueForTimeRange
            LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0);
            double todayRevenue = paymentService.getRevenueForTimeRange(startOfDay, endOfDay);

            // Costruzione del riepilogo
            Map<String, Object> summary = new HashMap<>();

            Map<String, Object> userStats = new HashMap<>();
            userStats.put("totalUsers", totalUsers);
            userStats.put("premiumUsers", premiumUsers);

            Map<String, Object> parkingStats = new HashMap<>();
            parkingStats.put("totalSpots", totalSpots);
            parkingStats.put("occupiedSpots", occupiedSpots);
            parkingStats.put("occupancyRate", Math.round(occupancyRate * 10) / 10.0);

            Map<String, Object> mwbotStats = new HashMap<>();
            mwbotStats.put("totalMwbots", totalMwbots);
            mwbotStats.put("availableMwbots", availableMwbots);
            mwbotStats.put("busyMwbots", busyMwbots);
            mwbotStats.put("maintenanceMwbots", maintenanceMwbots);

            summary.put("users", userStats);
            summary.put("parking", parkingStats);
            summary.put("mwbots", mwbotStats);
            summary.put("todayRevenue", todayRevenue);
            summary.put("currency", "EUR");

            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get dashboard summary: " + e.getMessage()));
        }
    }
}