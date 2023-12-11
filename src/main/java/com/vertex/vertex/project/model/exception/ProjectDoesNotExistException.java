package com.vertex.vertex.project.model.exception;


public class ProjectDoesNotExistException extends RuntimeException{
    public ProjectDoesNotExistException(Long projectId){
        super("Projeto com ID " + projectId + " não foi encontrado!");
    }
}
