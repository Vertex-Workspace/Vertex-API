package com.vertex.vertex.property.model.exceptions;

public class CantDeleteStatusException extends RuntimeException{

    public CantDeleteStatusException() {
        super("As propriedades status e data não podem ser removidas do sistema.");
    }
}
