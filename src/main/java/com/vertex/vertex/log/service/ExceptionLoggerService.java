package com.vertex.vertex.log.service;

import com.google.gson.Gson;
import com.vertex.vertex.log.kafka.KafkaService;
import com.vertex.vertex.log.model.entity.ExceptionLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExceptionLoggerService {

    @PersistenceContext
    private EntityManager entityManager;
    private final KafkaService kafkaService;

    @Transactional
    public void logException(Exception exception) {
        entityManager.persist(new ExceptionLog(exception));
        kafkaService.sendMessage(new Gson().toJson(new ExceptionLog(exception)));
    }

}