package com.parkingsystem.backend.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingsystem.backend.config.MqttTopics;

/**
 * Service per pubblicare messaggi MQTT direttamente.
 */
@Service
public class MqttPublisherService {

    private static final Logger logger = LoggerFactory.getLogger(MqttPublisherService.class);
    
    @Autowired
    private IMqttClient mqttClient;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Pubblica un comando per iniziare una ricarica direttamente tramite MQTT
     */
    public void publishChargingCommand(String botId, Long chargingRequestId, String parkingSpotId, int targetPercentage) {
        try {
            Map<String, Object> payload = Map.of(
                "botId", botId,
                "requestId", chargingRequestId,
                "parkingSpotId", parkingSpotId,
                "targetPercentage", targetPercentage,
                "command", "START_CHARGING",
                "timestamp", LocalDateTime.now().toString()
            );
            
            String topic = MqttTopics.formatTopic(MqttTopics.MWBOT_COMMAND, botId);
            publishMessage(topic, payload);
            
            logger.info("Published charging command for bot {} and request {}", botId, chargingRequestId);
        } catch (Exception e) {
            logger.error("Error publishing charging command: " + e.getMessage(), e);
        }
    }
    
    /**
     * Pubblica un comando per fermare una ricarica direttamente tramite MQTT
     */
    public void publishStopChargingCommand(String botId, Long chargingRequestId) {
        try {
            Map<String, Object> payload = Map.of(
                "botId", botId,
                "requestId", chargingRequestId,
                "command", "STOP_CHARGING",
                "timestamp", LocalDateTime.now().toString()
            );
            
            String topic = MqttTopics.formatTopic(MqttTopics.MWBOT_COMMAND, botId);
            publishMessage(topic, payload);
            
            logger.info("Published stop charging command for bot {} and request {}", botId, chargingRequestId);
        } catch (Exception e) {
            logger.error("Error publishing stop charging command: " + e.getMessage(), e);
        }
    }
    
    /**
     * Pubblica una notifica per l'utente direttamente tramite MQTT
     */
    public void publishUserNotification(Long userId, String message, String type) {
        try {
            Map<String, Object> payload = Map.of(
                "userId", userId,
                "message", message,
                "type", type,
                "timestamp", LocalDateTime.now().toString()
            );
            
            String topic = MqttTopics.formatTopic(MqttTopics.NOTIFICATION_USER, userId.toString());
            publishMessage(topic, payload);
            
            logger.info("Published notification for user {}: {}", userId, message);
        } catch (Exception e) {
            logger.error("Error publishing notification: " + e.getMessage(), e);
        }
    }
    
    /**
     * Metodo generico per pubblicare messaggi MQTT
     */
    public void publishMessage(String topic, Map<String, Object> payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            MqttMessage message = new MqttMessage(json.getBytes());
            message.setQos(1); // QoS 1 = at least once
            
            // Controlla se il client è connesso, altrimenti riconnetti
            if (!mqttClient.isConnected()) {
                logger.info("MQTT client not connected, attempting to reconnect");
                mqttClient.connect();
            }
            
            mqttClient.publish(topic, message);
            logger.debug("Published to MQTT: {} -> {}", topic, json);
        } catch (MqttException e) {
            logger.error("MQTT exception when publishing: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing JSON: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error publishing MQTT message: " + e.getMessage(), e);
        }
    }
}