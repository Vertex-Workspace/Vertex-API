package com.vertex.vertex.property.model.exceptions;

public class CantCreateOtherStatusException extends RuntimeException{

    public CantCreateOtherStatusException() {
        super("Não pode ser criada outra propriedade do tipo status");
    }
}
