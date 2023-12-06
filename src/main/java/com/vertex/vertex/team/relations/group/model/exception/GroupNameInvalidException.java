package com.vertex.vertex.team.relations.group.model.exception;

public class GroupNameInvalidException extends RuntimeException{
    public GroupNameInvalidException(){
        super("O nome do grupo deve ser preenchido!");
    }
}
