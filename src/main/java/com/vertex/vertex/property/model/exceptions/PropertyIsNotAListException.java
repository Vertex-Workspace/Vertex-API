package com.vertex.vertex.property.model.exceptions;

public class PropertyIsNotAListException extends RuntimeException{

    public PropertyIsNotAListException() {
        super("To add a list, it must be a status or a list");
    }
}
