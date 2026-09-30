// Modifica dell'AdminController esistente
package com.parkingsystem.frontend.controller;

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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BackendService backendService;

    // Metodo per controllare la validità della sessione
    private boolean isSessionInvalid(UserSession userSession) {
        return userSession == null || !userSession.isAuthenticated() || !userSession.isAdmin();
    }

    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";
        
        model.addAttribute("user", userSession);

        try {
            // Ottiene i dati della dashboard dal backend
            Map<String, Object> dashboardData = backendService.get("/admin/dashboard/summary", Map.class, userSession.getToken()).block();
            model.addAttribute("dashboardData", dashboardData);
            
            // Ottiene i primi 5 posti auto
            Map<String, Object>[] parkingSpots = backendService.get("/admin/spots", Map[].class, userSession.getToken()).block();
            model.addAttribute("parkingSpots", parkingSpots);
            
            // Ottiene i primi 5 MWbot
            Map<String, Object>[] mwbots = backendService.get("/admin/mwbots", Map[].class, userSession.getToken()).block();
            model.addAttribute("mwbots", mwbots);
        } catch (Exception e) {
            model.addAttribute("error", "Errore nel caricamento dei dati: " + e.getMessage());
        }

        return "admin/dashboard";
    }

    @GetMapping("/spots")
    public String manageParkingSpots(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";
        
        model.addAttribute("user", userSession);

        try {
            Map<String, Object>[] parkingSpots = backendService.get("/admin/spots", Map[].class, userSession.getToken()).block();
            model.addAttribute("parkingSpots", parkingSpots);
        } catch (Exception e) {
            model.addAttribute("error", "Errore nel caricamento dei dati: " + e.getMessage());
        }

        return "admin/spots";
    }

    @GetMapping("/spots/create")
    public String showCreateParkingSpotForm(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";
        
        model.addAttribute("user", userSession);
        return "admin/create-spot";
    }

    @PostMapping("/spots/create")
    public String createParkingSpot(
            @RequestParam String spotNumber,
            @RequestParam(defaultValue = "false") boolean chargingAvailable,
            @RequestParam String location,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("spotNumber", spotNumber);
            requestBody.put("chargingAvailable", chargingAvailable);
            requestBody.put("location", location);
            
            backendService.post("/admin/spots", requestBody, Map.class, userSession.getToken()).block();
            redirectAttributes.addFlashAttribute("success", "Posto auto creato con successo");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nella creazione del posto auto: " + e.getMessage());
        }

        return "redirect:/admin/spots";
    }

    @GetMapping("/mwbots")
    public String manageMWbots(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";
        
        model.addAttribute("user", userSession);

        try {
            Map<String, Object>[] mwbots = backendService.get("/admin/mwbots", Map[].class, userSession.getToken()).block();
            model.addAttribute("mwbots", mwbots);
        } catch (Exception e) {
            model.addAttribute("error", "Errore nel caricamento dei dati: " + e.getMessage());
        }

        return "admin/mwbots";
    }

    @GetMapping("/mwbots/create")
    public String showCreateMWbotForm(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";
        
        model.addAttribute("user", userSession);
        return "admin/create-mwbot";
    }

    @PostMapping("/mwbots/create")
    public String createMWbot(
            @RequestParam String botId,
            @RequestParam(defaultValue = "100") Integer batteryLevel,
            @RequestParam(defaultValue = "11.0") Double chargingRateKw,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("botId", botId);
            requestBody.put("batteryLevel", batteryLevel);
            requestBody.put("chargingRateKw", chargingRateKw);
            requestBody.put("status", "AVAILABLE");
            requestBody.put("maintenanceRequired", false);
            
            backendService.post("/admin/mwbots", requestBody, Map.class, userSession.getToken()).block();
            redirectAttributes.addFlashAttribute("success", "MWbot creato con successo");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nella creazione dell'MWbot: " + e.getMessage());
        }

        return "redirect:/admin/mwbots";
    }

    @GetMapping("/charging/assign")
    public String assignMWbot(HttpSession session, RedirectAttributes redirectAttributes) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";

        try {
            backendService.post("/admin/mwbots/assign", null, Map.class, userSession.getToken()).block();
            redirectAttributes.addFlashAttribute("success", "MWbot assegnato con successo alla prossima richiesta di ricarica");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nell'assegnazione dell'MWbot: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/settings")
    public String showSettings(HttpSession session, Model model) {
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";
        
        model.addAttribute("user", userSession);

        try {
            Map<String, Object> rates = backendService.get("/admin/rates", Map.class, userSession.getToken()).block();
            model.addAttribute("rates", rates);
        } catch (Exception e) {
            model.addAttribute("error", "Errore nel caricamento dei dati: " + e.getMessage());
        }

        return "admin/settings";
    }

    @PostMapping("/settings/rates")
    public String updateRates(
            @RequestParam Double parkingRate,
            @RequestParam Double chargingRate,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        if (isSessionInvalid(userSession)) return "redirect:/login";

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("baseRate", parkingRate);
            requestBody.put("chargingRate", chargingRate);
            
            backendService.put("/admin/rates", requestBody, Map.class, userSession.getToken()).block();
            redirectAttributes.addFlashAttribute("success", "Tariffe aggiornate con successo");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Errore nell'aggiornamento delle tariffe: " + e.getMessage());
        }

        return "redirect:/admin/settings";
    }
}