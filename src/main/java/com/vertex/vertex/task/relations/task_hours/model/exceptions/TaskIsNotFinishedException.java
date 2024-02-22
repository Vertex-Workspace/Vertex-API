package com.vertex.vertex.task.relations.task_hours.model.exceptions;

public class TaskIsNotFinishedException extends RuntimeException{

    public TaskIsNotFinishedException(String valor) {
        super("A tarefa '"+valor+ "' já está aberta. Finalize-a primeiro para começar outra");
    }
}
