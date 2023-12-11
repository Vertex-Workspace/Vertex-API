package com.vertex.vertex.team.relations.group.model.exception;

public class GroupNotFoundException extends RuntimeException{
    public GroupNotFoundException(Long groupId){
        super("O grupo com o ID " + groupId + " não foi encontrado!");
    }
}
