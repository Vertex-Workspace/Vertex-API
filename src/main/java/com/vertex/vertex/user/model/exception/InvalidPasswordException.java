package com.vertex.vertex.user.model.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("As senhas não coincidem!");
    }
}
