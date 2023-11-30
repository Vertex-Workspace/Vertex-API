package com.vertex.vertex.task.model.exceptions;

public class NotFoundValueInListException extends RuntimeException{

    public NotFoundValueInListException() {
        super("Não há esse elemento na lista atribuída");
    }
}
