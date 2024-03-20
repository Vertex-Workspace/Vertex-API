package com.vertex.vertex.project.model.ENUM;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ProjectReviewENUM {
    MANDATORY("Revisão Obrigatória"),
    OPTIONAL("Opcional"),
    EMPTY("Sem Revisão");

    private final String option;
}

