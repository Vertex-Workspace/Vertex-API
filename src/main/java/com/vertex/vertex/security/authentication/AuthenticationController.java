package com.vertex.vertex.security.authentication;

import com.vertex.vertex.user.model.DTO.UserLoginDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            HttpServletResponse response
    ) {
        throw new RuntimeException("ERROR 404!");
//        try {
//            return new ResponseEntity<>
//                    (authService.login(user, response),
//                            HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>
//                    ("E-mail ou senha inválidos!",
//                            HttpStatus.UNAUTHORIZED);
//        }
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

    @GetMapping("/user/confirm-email/{email}")
    public ResponseEntity<?> confirmEmail(
            @PathVariable String email,
            HttpServletResponse response) {
        try {
            authService.confirmEmail(email, response);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}