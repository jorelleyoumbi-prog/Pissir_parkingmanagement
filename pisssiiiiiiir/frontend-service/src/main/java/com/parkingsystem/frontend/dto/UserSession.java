package com.parkingsystem.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {
    private Long id;
    private String username;
    private String role;
    private String token;
    private String email;
    private String fullName;
    private boolean authenticated;

    public UserSession() {}

    public UserSession(Long id, String username, String role, String token, String email, String fullName, boolean authenticated) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.token = token;
        this.email = email;
        this.fullName = fullName;
        this.authenticated = authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isPremium() {
        return "PREMIUM".equalsIgnoreCase(role);
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
}
