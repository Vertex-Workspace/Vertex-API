package com.vertex.vertex.team.model.exceptions;

public class TeamNotFoundException extends RuntimeException{
    public TeamNotFoundException(Long teamId){
        super("Equipe com ID " + teamId + " não foi encontrada!");
    }
}
