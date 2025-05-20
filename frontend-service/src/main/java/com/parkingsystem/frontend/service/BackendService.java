
package com.parkingsystem.frontend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class BackendService {

    @Autowired
    private WebClient backendClient;
    
    @Value("${backend.url}")
    private String backendUrl;
    
    @Bean
    public WebClient backendClient() {
        return WebClient.builder()
                .baseUrl(backendUrl)
                .build();
    }

    public <T> Mono<T> get(String path, Class<T> responseType) {
        return backendClient.get()
                .uri(path)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> get(String path, Class<T> responseType, String token) {
        return backendClient.get()
                .uri(path)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> post(String path, Object requestBody, Class<T> responseType) {
        return backendClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> post(String path, Object requestBody, Class<T> responseType, String token) {
        return backendClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> put(String path, Object requestBody, Class<T> responseType, String token) {
        return backendClient.put()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }
}