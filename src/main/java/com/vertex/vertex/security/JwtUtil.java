package com.vertex.vertex.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@Component
public class JwtUtil {
    private final Environment environment;


    public String generateToken(UserDetails userDetails){
        Algorithm algorithm = Algorithm.HMAC256(Objects.requireNonNull(environment.getProperty("secret.key")));
        return JWT.create().
                withIssuer("WEG")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + 300000))
                .withSubject(userDetails.getUsername())
                .sign(algorithm);
    }


    public String getUsername(String token) {
        return JWT.decode(token).getSubject();
    }
}
