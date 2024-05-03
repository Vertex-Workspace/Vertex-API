package com.vertex.vertex.security;

import com.vertex.vertex.user.model.DTO.UserLoginDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final Environment environment;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public User login(UserLoginDTO user,
                      HttpServletRequest request,
                      HttpServletResponse response) {
        try {
            CookieUtils cookieUtil = new CookieUtils(environment);
            //Principal - username
            //Credential - password
            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

            //Interface genérica para autenticação
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            //Gera cookie com o token JWT e o adiciona na resposta da request
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Cookie cookie = cookieUtil.generateCookieJWT(userDetails);
            response.addCookie(cookie);

            return userService.findByEmail(userDetails.getUsername());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User externalServiceLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            UserDetails userDetails) {
        CookieUtils cookieUtil = new CookieUtils(environment);
        Cookie cookie = cookieUtil.generateCookieJWT(userDetails);
        response.addCookie(cookie);
        return userService.findByEmail(userDetails.getUsername());
    }

    public Cookie logout(HttpServletRequest request) {
        try {
            CookieUtils cookieUtil = new CookieUtils(environment);
            Cookie cookie = cookieUtil.getCookie(request, "JWT");
            cookie.setMaxAge(0);
            return cookie;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public User getAuthenticatedUser() {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder
                        .getContext().getAuthentication()
                        .getPrincipal();
        return userService.findByEmail(userDetails.getUsername());
    }



}
