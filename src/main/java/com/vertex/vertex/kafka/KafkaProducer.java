package com.vertex.vertex.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String TOPIC = "test-topic";

    public void sendMessage(NotificationDTO message) {
        log.info("MESSAGE SENT FROM PRODUCER END -> " + message);
        kafkaTemplate.send(TOPIC, message.toString());
    }
}
