package com.parkingsystem.frontend.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.parkingsystem.frontend.dto.UserSession;
import com.parkingsystem.frontend.service.BackendService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BackendService backendService;
    
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @GetMapping
    public String bookingForm(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        
        if (userSession == null || !userSession.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Solo gli utenti premium possono prenotare
        if (!userSession.isPremium()) {
            return "redirect:/dashboard";
        }
        
        model.addAttribute("user", userSession);
        
        try {
            // Ottiene le prenotazioni attive dell'utente
            Map<String, Object>[] reservations = backendService.get(
                    "/premium/reservations", 
                    Map[].class, 
                    userSession.getToken()
            ).block();
            
            model.addAttribute("reservations", reservations);
            
            // Ottiene i posti disponibili per le prossime 24 ore
            LocalDateTime startTime = LocalDateTime.now().plusHours(1);
            LocalDateTime endTime = startTime.plusHours(2);
            
            Map<String, Object>[] availableSpots = backendService.get(
                    "/premium/availability?startTime=" + startTime.format(dateTimeFormatter) + 
                    "&endTime=" + endTime.format(dateTimeFormatter) + 
                    "&chargingRequired=false",
                    Map[].class,
                    userSession.getToken()
            ).block();
            
            model.addAttribute("availableSpots", availableSpots);
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
            
        } catch (Exception e) {
            model.addAttribute("error", "Errore nel caricamento dei dati: " + e.getMessage());
        }
        
        return "booking";
    }
    
    @PostMapping("/check-availability")
    public String checkAvailability(
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(required = false, defaultValue = "false") boolean chargingRequired,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        
        if (userSession == null || !userSession.isAuthenticated() || !userSession.isPremium()) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", userSession);
        
        try {
            // Formatta le date ISO
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);
            
            if (start.isAfter(end)) {
                redirectAttributes.addFlashAttribute("error", "La data di inizio deve essere precedente alla data di fine");
                return "redirect:/booking";
            }
            
            if (start.isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("error", "La data di inizio deve essere nel futuro");
                return "redirect:/booking";
            }
            
            // Ottiene i posti disponibili per l'intervallo selezionato
            Map<String, Object>[] availableSpots = backendService.get(
                    "/premium/availability?startTime=" + start.format(dateTimeFormatter) + 
                    "&endTime=" + end.format(dateTimeFormatter) + 
                    "&chargingRequired=" + chargingRequired,
                    Map[].class,
                    userSession.getToken()
            ).block();
            
            redirectAttributes.addFlashAttribute("availableSpots", availableSpots);
            redirectAttributes.addFlashAttribute("startTime", start);
            redirectAttributes.addFlashAttribute("endTime", end);
            redirectAttributes.addFlashAttribute("chargingRequired", chargingRequired);
            
            return "redirect:/booking";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nel controllo disponibilità: " + e.getMessage());
            return "redirect:/booking";
        }
    }
    
    @PostMapping("/reserve")
    public String reserve(
            @RequestParam Long parkingSpotId,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(required = false, defaultValue = "false") boolean chargingRequired,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        
        if (userSession == null || !userSession.isAuthenticated() || !userSession.isPremium()) {
            return "redirect:/login";
        }
        
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("parkingSpotId", parkingSpotId);
            requestBody.put("startTime", startTime);
            requestBody.put("endTime", endTime);
            requestBody.put("chargingRequired", chargingRequired);
            
            Map<String, Object> response = backendService.post(
                    "/premium/reservations",
                    requestBody,
                    Map.class,
                    userSession.getToken()
            ).block();
            
            if (response != null && response.containsKey("id")) {
                redirectAttributes.addFlashAttribute("success", "Prenotazione effettuata con successo");
            } else {
                redirectAttributes.addFlashAttribute("error", "Errore nella prenotazione");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nella prenotazione: " + e.getMessage());
        }
        
        return "redirect:/booking";
    }
    
    @PostMapping("/cancel")
    public String cancelReservation(
            @RequestParam Long reservationId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        
        if (userSession == null || !userSession.isAuthenticated()) {
            return "redirect:/login";
        }
        
        try {
            // Chiamata al backend per cancellare la prenotazione
            backendService.post(
                    "/reservations/" + reservationId + "/cancel",
                    null,
                    Map.class,
                    userSession.getToken()
            ).block();
            
            redirectAttributes.addFlashAttribute("success", "Prenotazione cancellata con successo");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nella cancellazione: " + e.getMessage());
        }
        
        return "redirect:/booking";
    }
}