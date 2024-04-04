package com.vertex.vertex.notification.repository;

import com.vertex.vertex.notification.entity.model.LogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository
        extends JpaRepository<LogRecord, Long> {
}
