package com.parkingsystem.backend.publisher;

import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MqttNotificationPublisher implements NotificationPublisher {

    private final MqttClient mqttClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MqttNotificationPublisher() throws MqttException {

        this.mqttClient = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId(), new MemoryPersistence());
        this.mqttClient.connect();
    }

    @Override
    public void publish(String topic, Map<String, Object> payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            MqttMessage message = new MqttMessage(json.getBytes());
            message.setQos(1); // QoS 1 = at least once
            mqttClient.publish(topic, message);
            System.out.println("✅ Published to MQTT: " + topic + " -> " + json);
        } catch (Exception e) {
            System.err.println("❌ MQTT publish error: " + e.getMessage());
        }
    }
}
