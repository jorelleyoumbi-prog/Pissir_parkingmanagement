package com.parkingsystem.mqtt;


import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttOutboundGateway {

    // topic dinamico via header
    void sendToMqtt(
      @Header("mqtt_topic") String topic,
      @Payload String payload
    );
}
