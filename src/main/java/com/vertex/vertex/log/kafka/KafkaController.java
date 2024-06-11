package com.vertex.vertex.log.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vertex.vertex.log.model.entity.ExceptionLog;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value = "/logs")
@AllArgsConstructor
@CrossOrigin
public class KafkaController {

    private final KafkaService service;
    @GetMapping
    public Collection<ExceptionLog> getErrorLogs() throws JsonProcessingException {
        return service.getAllMessages();
    }
}
