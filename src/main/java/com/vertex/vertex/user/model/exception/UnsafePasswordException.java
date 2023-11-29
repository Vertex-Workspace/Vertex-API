package com.vertex.vertex.user.model.exception;

public class UnsafePasswordException extends RuntimeException {

    public UnsafePasswordException() {
        super("A senha não atende aos requisitos mínimos do sistema!");
    }

}
