//package com.vertex.vertex.kafka;
//
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.logging.Logger;
//
//@Component
//public class KafkaConsumer {
//    private final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
//
//    @Autowired
//    private KafkaService kafkaService;
//
//    @KafkaListener(topics = "test-topic", groupId ="xyz")
//    public void consume(String message) {
//        log.info("MESSAGE RECEIVED AT CONSUMER END -> " + message);
//        kafkaService.addMessage(message);
//    }
//}
