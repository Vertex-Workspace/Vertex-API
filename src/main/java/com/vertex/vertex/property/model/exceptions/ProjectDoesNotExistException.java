package com.vertex.vertex.property.model.exceptions;

public class ProjectDoesNotExistException extends RuntimeException{

    public ProjectDoesNotExistException() {
        super("There isn't a project with this id to add a property");
    }
}
