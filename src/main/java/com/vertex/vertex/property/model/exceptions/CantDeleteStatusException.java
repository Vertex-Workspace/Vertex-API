package com.vertex.vertex.property.model.exceptions;

public class CantDeleteStatusException extends RuntimeException{

    public CantDeleteStatusException() {
        super("A propriedade status não pode ser removida do sistema.");
    }
}
