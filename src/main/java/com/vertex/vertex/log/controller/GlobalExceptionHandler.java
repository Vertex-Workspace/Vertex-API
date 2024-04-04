package com.vertex.vertex.log.controller;

import com.vertex.vertex.log.service.ExceptionLoggerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionLoggerService exceptionLogger;

    @ExceptionHandler(Exception.class)
    public void handleException(Exception exception) {
        exceptionLogger.logException(exception);
    }

}
