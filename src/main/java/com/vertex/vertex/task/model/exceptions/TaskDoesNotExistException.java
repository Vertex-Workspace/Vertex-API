package com.vertex.vertex.task.model.exceptions;

public class TaskDoesNotExistException extends RuntimeException{

    public TaskDoesNotExistException() {
        super("Não existe uma tarefa com esse id");
    }
}
