package com.parkingsystem.backend.publisher;

import java.util.Map;

public interface NotificationPublisher {
    void publish(String topic, Map<String, Object> payload);
}
