package com.vertex.vertex.security;

import com.vertex.vertex.security.CookieUtils;
import com.vertex.vertex.user.model.DTO.UserLoginDTO;
import com.vertex.vertex.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping
@CrossOrigin
public class AuthenticationController {

    private final AuthService authService;

    //Manipular a requisição de forma única e personalizável
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @RequestBody UserLoginDTO user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            return new ResponseEntity<>
                    (authService.login(user, request, response),
                            HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>
                    ("E-mail ou senha inválidos!",
                            HttpStatus.UNAUTHORIZED);
        }
    }

    //Seta cookie com tempo "0" excluindo ele do navegador do usuário
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            for (Cookie c : authService.logout(request)) {
                response.addCookie(c);
            }
        } catch (Exception e) {
            response.setStatus(401);
        }
    }

    @GetMapping("/authenticate-user")
    public ResponseEntity<?> getAuthenticationUser() {
        try {
            return new ResponseEntity<>
                    (authService.getAuthenticatedUser(),
                            HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}