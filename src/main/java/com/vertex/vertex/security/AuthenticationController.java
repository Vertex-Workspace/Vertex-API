package net.weg.otavio.security;

import com.vertex.vertex.security.CookieUtils;
import com.vertex.vertex.user.model.DTO.UserLoginDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final Environment environment;

    //Manipular a requisição de forma única e personalizável
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserLoginDTO user,
                                          HttpServletRequest request, HttpServletResponse response){
        try{
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
            return new ResponseEntity<>("Autenticação bem Sucedida!", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Falha na autenticação!", HttpStatus.UNAUTHORIZED);
        }
    }

    //Seta cookie com tempo "0" excluindo ele do navegador do usuário
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        try {
            CookieUtils cookieUtil = new CookieUtils(environment);
            Cookie cookie = cookieUtil.getCookie(request, "JWT");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        } catch (Exception e) {
            response.setStatus(401);
        }
    }
}