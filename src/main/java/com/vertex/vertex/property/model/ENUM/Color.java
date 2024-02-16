package com.vertex.vertex.property.model.ENUM;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {

    BLUE("a"),
    RED("#FF9D9D"),
    PURPLE("a"),
    PINK("a"),
    ORANGE("a"),
    GREY("a"),
    YELLOW("#FFD600"),
    BROWN("a"),
    GREEN("#65D73C");

    private final String color;

    public String getColor() {
        return color;
    }
    
}
