package com.vertex.vertex.task.relations.review.model.ENUM;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public enum ApproveStatus {

    APPROVED("Aprovada"),
    DISAPPROVED("Reprovada"),
    UNDERANALYSIS("Sob Análise");

    private final String name;

    public String getName(){
        return name;
    }
}
