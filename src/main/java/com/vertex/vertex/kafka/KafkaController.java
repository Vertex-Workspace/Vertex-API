package com.vertex.vertex.kafka;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@AllArgsConstructor
public class KafkaController {
    private final KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<HttpStatus> sendMessage(@RequestBody NotificationDTO notificationDTO){
        try{
            kafkaProducer.sendMessage(notificationDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
