package com.vertex.vertex.log.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vertex.vertex.log.model.entity.ExceptionLog;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class KafkaService {

    private static final String topic = "error_log";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message){
        System.out.println("sendMessage (kafka service)");
        this.kafkaTemplate.send(topic,message);
    }
    @KafkaListener(topics = "error_log", groupId = "vertex")
    public ExceptionLog getMessages(String message){
        return new ObjectMapper().convertValue(message, ExceptionLog.class);
    }
//    public void save(){
//
//    }
}
