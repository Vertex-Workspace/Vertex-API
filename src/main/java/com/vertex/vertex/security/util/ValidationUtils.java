package com.vertex.vertex.security.util;

import com.vertex.vertex.project.model.entity.Project;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.task_responsables.model.entity.TaskResponsable;
import com.vertex.vertex.team.relations.user_team.model.entity.UserTeam;
import com.vertex.vertex.user.model.entity.User;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class ValidationUtils {

    public static void validateUserLogged(String email){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userDetails.getUsername().equals(email)){
            throw new AuthenticationCredentialsNotFoundException("Não autorizado!");
        }
    }
    public static void validateUserLogged(List<String> emails){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (String email : emails){
            if(userDetails.getUsername().equals(email)){
                return;
            }
        }
        throw new AuthenticationCredentialsNotFoundException("Não autorizado!");
    }

    public static void loggedUserIsOnTask(Task task){
        if(task.getTaskResponsables()
                .stream()
                .map(TaskResponsable::getUserTeam)
                .map(UserTeam::getUser)
                .anyMatch(user -> user.getEmail().equals((
                        (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername()
                ))){
            return;
        }
        throw new AuthenticationCredentialsNotFoundException("Usuário não pertence a tarefa!");
    }

    public static void loggedUserIsOnTaskAndIsCreator(Task task){
        loggedUserIsOnTask(task);
        if(task.getCreator().getUser().getEmail().equals(
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername())){
            return;
        }
        throw new AuthenticationCredentialsNotFoundException("Usuário não é criador da tarefa!");
    }

    public static void loggedUserIsOnProject(Project project){
        if(project.getCollaborators()
                .stream()
                .map(UserTeam::getUser)
                .anyMatch(user -> user.getEmail().equals(
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getEmail()))){
            return;
        }
        throw new AuthenticationCredentialsNotFoundException("Usuário não faz parte do projeto!");
    }
    public static void loggedUserIsOnProjectAndIsCreator(Project project){
        loggedUserIsOnProject(project);
        if(project.getCreator().getUser().getEmail().equals(
                ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getUsername())){
            return;
        }
        throw new AuthenticationCredentialsNotFoundException("Usuário não é criador do projeto!");
    }
}
