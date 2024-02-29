package com.vertex.vertex.property.model.ENUM;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {

    BLUE("BLUE"),
    RED("#FF9D9D"),
    PURPLE("PURPLE"),
    PINK("PINK"),
    ORANGE("ORANGE"),
    GREY("GREY"),
    YELLOW("#FFD600"),
    BROWN("BROWN"),
    GREEN("#65D73C");

    private final String color;

    public String getColor() {
        return color;
    }
    
}
