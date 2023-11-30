package com.vertex.vertex.task.model.exceptions;

public class NotFoundPropertyInTaskException extends RuntimeException{

    public NotFoundPropertyInTaskException() {
        super("A tarefa não pode ser criada, pois referencia uma propriedade que não pertence a seu projeto");
    }
}
