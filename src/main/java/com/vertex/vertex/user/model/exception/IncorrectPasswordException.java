package com.vertex.vertex.user.model.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException() {
        super("Senha incorreta!");
    }
    
}
