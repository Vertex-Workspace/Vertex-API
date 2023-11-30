package com.vertex.vertex.user.model.exception;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException() {
        super("E-mail inválido!");
    }

}
