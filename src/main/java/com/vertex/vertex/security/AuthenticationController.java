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

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final Environment environment;

    //Manipular a requisição de forma única e personalizável
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserLoginDTO user,
                                          HttpServletRequest request, HttpServletResponse response) {
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
            return new ResponseEntity<>(userService.findByEmail(userDetails.getUsername()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("E-mail ou senha inválidos!", HttpStatus.UNAUTHORIZED);
        }
    }

    //Seta cookie com tempo "0" excluindo ele do navegador do usuário
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            CookieUtils cookieUtil = new CookieUtils(environment);
            Cookie cookie = cookieUtil.getCookie(request, "JWT");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        } catch (Exception e) {
            response.setStatus(401);
        }
    }

    @GetMapping("/authenticate-user")
    public ResponseEntity<?> getAuthenticationUser() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new ResponseEntity<>(userService.findByEmail(userDetails.getUsername()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}