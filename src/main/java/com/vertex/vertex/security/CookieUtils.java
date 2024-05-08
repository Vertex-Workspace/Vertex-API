package com.vertex.vertex.security;

import com.vertex.vertex.task.model.entity.Task;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class CookieUtils {

    private final Environment environment;


    public Cookie generateCookieJWT(UserDetails userDetails){
        String token = new JwtUtil(environment).generateToken(userDetails);
        Cookie cookie =  new Cookie("JWT", token);
        //Onde o cookie pode ser acessado, nesse caso todos os endpoints
        cookie.setPath("/");
        cookie.setMaxAge(18000);
        return cookie;
    }

    public Cookie getCookie(HttpServletRequest request, String name){
        Cookie cookie = WebUtils.getCookie(request, name);
        if(cookie != null){
            return cookie;
        }
        throw new AuthenticationCredentialsNotFoundException("Usuário não autenticado");
    }
}