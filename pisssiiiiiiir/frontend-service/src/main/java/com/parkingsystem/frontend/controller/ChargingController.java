package com.parkingsystem.frontend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.parkingsystem.frontend.dto.UserSession;
import com.parkingsystem.frontend.service.BackendService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/charging")
public class ChargingController {

    @Autowired
    private BackendService backendService;

    @GetMapping
    public String chargingPage(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        
        if (userSession == null || !userSession.isAuthenticated()) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", userSession);
        
        try {
            // Ottieni le richieste di ricarica attive dell'utente
            Map<String, Object>[] chargingRequests = backendService.get(
                    "/charging/user", 
                    Map[].class, 
                    userSession.getToken()
            ).block();
            
            model.addAttribute("chargingRequests", chargingRequests);
            
            // Ottieni i posti occupati dall'utente (dove può richiedere ricarica)
            Map<String, Object>[] parkingSpots = backendService.get(
                    "/parking/spots?occupied=true",
                    Map[].class,
                    userSession.getToken()
            ).block();
            
            model.addAttribute("parkingSpots", parkingSpots);
            
            // Ottieni i dettagli dell'auto per calcolare l'energia
            Map<String, Object> userDetails = backendService.get(
                    "/auth/me",
                    Map.class,
                    userSession.getToken()
            ).block();
            
            model.addAttribute("userDetails", userDetails);
            
        } catch (Exception e) {
            model.addAttribute("error", "Errore nel caricamento dei dati: " + e.getMessage());
        }
        
        return "charging";
    }
    
    @PostMapping("/request")
    public String requestCharging(
            @RequestParam Long parkingSpotId,
            @RequestParam Integer initialPercentage,
            @RequestParam Integer targetPercentage,
            @RequestParam(required = false, defaultValue = "false") boolean notificationRequested,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        
        if (userSession == null || !userSession.isAuthenticated()) {
            return "redirect:/login";
        }
        
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("parkingSpotId", parkingSpotId);
            requestBody.put("initialPercentage", initialPercentage);
            requestBody.put("targetPercentage", targetPercentage);
            requestBody.put("notificationRequested", notificationRequested);
            
            Map<String, Object> response = backendService.post(
                    "/charging/request",
                    requestBody,
                    Map.class,
                    userSession.getToken()
            ).block();
            
            if (response != null && response.containsKey("id")) {
                redirectAttributes.addFlashAttribute("success", "Richiesta di ricarica effettuata con successo");
            } else {
                redirectAttributes.addFlashAttribute("error", "Errore nella richiesta di ricarica");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nella richiesta di ricarica: " + e.getMessage());
        }
        
        return "redirect:/charging";
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelCharging(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        
        if (userSession == null || !userSession.isAuthenticated()) {
            return "redirect:/login";
        }
        
        try {
            backendService.post(
                    "/charging/" + id + "/cancel",
                    null,
                    Map.class,
                    userSession.getToken()
            ).block();
            
            redirectAttributes.addFlashAttribute("success", "Richiesta di ricarica annullata con successo");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nell'annullamento: " + e.getMessage());
        }
        
        return "redirect:/charging";
    }
    
    @GetMapping("/{id}/status")
    public String chargingStatus(
            @PathVariable Long id,
            HttpSession session,
            Model model) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        
        if (userSession == null || !userSession.isAuthenticated()) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", userSession);
        
        try {
            Map<String, Object> chargingRequest = backendService.get(
                    "/charging/" + id,
                    Map.class,
                    userSession.getToken()
            ).block();
            
            model.addAttribute("charging", chargingRequest);
        } catch (Exception e) {
            model.addAttribute("error", "Errore nel caricamento dei dati: " + e.getMessage());
        }
        
        return "charging-status";
    }
    
    @PostMapping("/{id}/additional")
    public String requestAdditionalCharging(
            @PathVariable Long id,
            @RequestParam Integer targetPercentage,
            @RequestParam(required = false, defaultValue = "false") boolean notificationRequested,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        
        if (userSession == null || !userSession.isAuthenticated()) {
            return "redirect:/login";
        }
        
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("targetPercentage", targetPercentage);
            requestBody.put("notificationRequested", notificationRequested);
            
            Map<String, Object> response = backendService.post(
                    "/charging/" + id + "/additional",
                    requestBody,
                    Map.class,
                    userSession.getToken()
            ).block();
            
            if (response != null && response.containsKey("id")) {
                redirectAttributes.addFlashAttribute("success", "Richiesta di ricarica aggiuntiva effettuata con successo");
            } else {
                redirectAttributes.addFlashAttribute("error", "Errore nella richiesta di ricarica aggiuntiva");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nella richiesta di ricarica aggiuntiva: " + e.getMessage());
        }
        
        return "redirect:/charging";
    }
}