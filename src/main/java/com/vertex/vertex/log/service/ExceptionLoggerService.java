package com.vertex.vertex.log.service;

import com.vertex.vertex.log.model.ExceptionLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ExceptionLoggerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void logException(Exception exception) {
        entityManager.persist(new ExceptionLog(exception));
    }

}
