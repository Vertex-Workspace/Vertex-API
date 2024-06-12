package com.vertex.vertex.log.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vertex.vertex.log.model.entity.ExceptionLog;
import org.apache.juli.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DestinationTopic;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Service
public class KafkaService {


    private static ArrayList<ExceptionLog> logs;
    private static final String topic = "error_logs";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message){
        System.out.println("sendMessage (kafka service)");
        this.kafkaTemplate.send(topic,message);

    }

    public Collection<ExceptionLog> getAllMessages() throws JsonProcessingException {

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "vertex");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(List.of(topic));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(60000 ));
        ArrayList<ExceptionLog> logs = new ArrayList<>();
        for( ConsumerRecord<String, String > record : records){
            System.out.println( record.value()
            );
            ExceptionLog exceptionLog = new Gson().fromJson(record.value(), ExceptionLog.class);
            System.out.println(exceptionLog);
            logs.add(exceptionLog);
        }
        consumer.close();
        System.out.println(logs);
        return logs;
//        return null;
    }
}
