package com.vertex.vertex.log.model.exception;

public class EntityDoesntExistException extends RuntimeException {
    public EntityDoesntExistException() {
        super("Entity does not exist");
    }
}
