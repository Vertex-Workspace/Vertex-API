package com.vertex.vertex.security.authentication;

import com.vertex.vertex.google.service.CalendarService;
import com.vertex.vertex.security.util.CookieUtils;
import com.vertex.vertex.user.model.DTO.UserDTO;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {

    private final Environment environment;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final CalendarService calendarService;

    public User login(UserLoginDTO user,
                      HttpServletResponse response) {
        try {
            CookieUtils cookieUtil = new CookieUtils(environment);
            //Principal - username
            //Credential - password
            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

            //Interface genérica para autenticação
            Authentication authentication
                    = authenticationManager.authenticate(authenticationToken);

            //Gera cookie com o token JWT e o adiciona na resposta da request
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Cookie cookie = cookieUtil.generateCookieJWT(userDetails);
            response.addCookie(cookie);
            firstAccess((User) authentication.getPrincipal());
            return userService.findByEmail(userDetails.getUsername());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void externalServiceLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            UserDetails userDetails) {
        CookieUtils cookieUtil = new CookieUtils(environment);
        Cookie cookie = cookieUtil.generateCookieJWT(userDetails);
        response.addCookie(cookie);
    }

    private void firstAccess(User user) {
        if (user.isFirstAccess()) {
            user.setFirstAccess(false);
        }
    }

    public List<Cookie> logout(HttpServletRequest request) {
        try {
            CookieUtils cookieUtil = new CookieUtils(environment);
            Cookie cookie = cookieUtil.getCookie(request, "JWT");
            Cookie cookie1 = cookieUtil.getCookie(request, "JSESSIONID");
            cookie1.setMaxAge(0);
            cookie.setMaxAge(0);

            return List.of(cookie1, cookie);
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

    public void initExternalServiceLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        System.out.println(oAuth2User+"OAUTH2");
        String email = oAuth2User.getAttribute("email");
        System.out.println(email +"EMAIL");

        try { // already registered
            UserDetails user = userDetailsService.loadUserByUsername(email);
            firstAccess((User) user);
            externalServiceLogin(request, response, user);

        } catch (UsernameNotFoundException e) { // first access
            User user = new User(email, oAuth2User);
            userService.save(new UserDTO(user, oAuth2User.getAttribute("picture")));
            externalServiceLogin(request, response, user);
        }
        response.sendRedirect("http://localhost:4200");
    }

    public void confirmEmail(
            String email,
            HttpServletResponse response) throws IOException {
        User user = userService.findByEmail(email);
        user.setEmailConfirm(true);
        userService.save(user);
        response.sendRedirect("http://localhost:4200/login");
    }




}
