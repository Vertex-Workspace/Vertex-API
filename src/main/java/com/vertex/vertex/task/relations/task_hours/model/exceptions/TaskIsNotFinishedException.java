package com.vertex.vertex.task.relations.task_hours.model.exceptions;

public class TaskIsNotFinishedException extends RuntimeException{

    public TaskIsNotFinishedException() {
        super("Já há uma tarefa aberta. Finalize-a primeiro para começar outra");
    }
}
