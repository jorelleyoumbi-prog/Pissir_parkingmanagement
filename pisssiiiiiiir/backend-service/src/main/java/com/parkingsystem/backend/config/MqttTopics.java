package com.parkingsystem.backend.config;


/**
 * Classe di utilità che contiene le costanti per i topic MQTT.
 * Usata per standardizzare la comunicazione tra backend e IoT Gateway.
 */
public class MqttTopics {
    // Prefisso comune per tutti i topic
    public static final String PREFIX = "parkingsystem";
    
    // Topic per i posti auto
    public static final String PARKING_SPOT_STATUS = PREFIX + "/parking/spots/%s/status";
    public static final String PARKING_ENTRANCE = PREFIX + "/parking/entrance";
    public static final String PARKING_EXIT = PREFIX + "/parking/exit";
    
    // Topic per i MWbot
    public static final String MWBOT_STATUS = PREFIX + "/mwbot/%s/status";
    public static final String MWBOT_COMMAND = PREFIX + "/mwbot/%s/command";
    
    // Topic per le ricariche
    public static final String CHARGING_REQUEST = PREFIX + "/charging/request";
    public static final String CHARGING_PROGRESS = PREFIX + "/charging/%s/progress";
    public static final String CHARGING_COMPLETED = PREFIX + "/charging/%s/completed";
    
    // Topic per le notifiche
    public static final String NOTIFICATION_USER = PREFIX + "/notification/user/%s";
    
    /**
     * Formatta un topic con il parametro specificato.
     * @param topic Il pattern del topic con %s
     * @param param Il parametro da inserire
     * @return Il topic formattato
     */
    public static String formatTopic(String topic, String param) {
        return String.format(topic, param);
    }
}