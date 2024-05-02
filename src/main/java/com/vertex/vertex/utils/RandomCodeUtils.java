package com.vertex.vertex.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;

@Data
@AllArgsConstructor
public class RandomCodeUtils {

    public static String generateInvitationCode() {
        String caracteres = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < caracteres.length(); i++) {
            char a = caracteres.charAt(random.nextInt(34));
            token.append(a);
        }
        return token.toString();
    }
}
