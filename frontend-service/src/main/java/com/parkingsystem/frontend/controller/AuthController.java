package com.parkingsystem.frontend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.parkingsystem.frontend.dto.UserSession;
import com.parkingsystem.frontend.service.BackendService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private BackendService backendService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        System.out.println("Login attempt for: " + username);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);

        try {
            System.out.println("Sending login request to backend...");
            Map<String, Object> response = backendService.post("/auth/login", requestBody, Map.class)
                    .block();
            
            System.out.println("Response received: " + response);

            if (response != null && response.containsKey("token")) {
                String token = response.get("token").toString();
                System.out.println("Token received: " + token.substring(0, 10) + "...");
                
                UserSession userSession = new UserSession(
                        Long.valueOf(response.get("id").toString()),
                        response.get("username").toString(),
                        response.get("role").toString(),
                        token,
                        response.containsKey("email") ? response.get("email").toString() : null,
                        response.containsKey("fullName") ? response.get("fullName").toString() : null,
                        true // authenticated
                );

                System.out.println("UserSession created: " + userSession.getUsername() + ", Role: " + userSession.getRole());
                session.setAttribute("userSession", userSession);
                System.out.println("Session attribute set");

                return "redirect:/dashboard";
            } else {
                System.out.println("Login failed: response valid but no token");
                redirectAttributes.addFlashAttribute("error", "Login fallito: risposta non valida dal server");
                return "redirect:/login";
            }
        } catch (Exception e) {
            System.out.println("Login exception: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Login fallito: " + e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam Map<String, String> formData,
                           RedirectAttributes redirectAttributes) {

        try {
            Map<String, Object> response = backendService.post("/auth/register", formData, Map.class)
                    .block();

            if (response != null && response.containsKey("id")) {
                redirectAttributes.addFlashAttribute("success", "Registrazione completata con successo. Effettua il login.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "Registrazione fallita: risposta non valida dal server");
                return "redirect:/register";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registrazione fallita: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    

}
