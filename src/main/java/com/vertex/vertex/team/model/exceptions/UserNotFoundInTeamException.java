package com.vertex.vertex.team.model.exceptions;

public class UserNotFoundInTeamException extends RuntimeException{
    public UserNotFoundInTeamException(){
        super("Usuário não foi encontrado na Equipe!");
    }
}
