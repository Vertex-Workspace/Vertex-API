package com.vertex.vertex.property.model.ENUM;

import lombok.AllArgsConstructor;

@AllArgsConstructor

public enum Color {

    BLUE("#d3e5ef"),
    RED("#ffe2dd"),
    PURPLE("#e8deee"),
    PINK("#f5e0e9"),
    ORANGE("#fadec9"),
    GREY("#e3e2e0"),
    YELLOW( "#fdecc8"),
    GREEN("#dbeddb");

    private final String hexadecimal;

    public String getHexadecimal(){
        return this.hexadecimal;
    }

}
