package com.vertex.vertex.security;

import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.DTO.UserLoginDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.socket.WebSocketHttpHeaders;

@Configuration
@AllArgsConstructor
public class SecurityConfig{

    private final FilterAuthentication filterAuthentication;
    private final SecurityContextRepository securityRepository;
    private final UserDetailsServiceImpl authenticationService;
    private final UserService userService;
    private final AuthService authService;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer ->
            httpSecurityCorsConfigurer.configurationSource(BeansConfig.corsConfigurationSource())
        );

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/authenticate-user").authenticated()
                .requestMatchers(WebSocketHttpHeaders.ALLOW, "/notifications", "/chat").permitAll()
                .anyRequest().authenticated()
        )
                .oauth2Login(httpOauth2 -> httpOauth2.successHandler((request, response, authentication) -> {
                    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                    String email = oAuth2User.getAttribute("email");

                    try {
                        UserDetails user = authenticationService.loadUserByUsername(email);
                        authService.externalServiceLogin(request, response, user);
                        response.sendRedirect("http://localhost:4200/home");

                    } catch (UsernameNotFoundException e) {
                        String lastName = oAuth2User.getAttribute("family_name");
                        String firstName = oAuth2User.getAttribute("name");
                        firstName = firstName.substring(0, firstName.indexOf(" "));

                        User user = new User();
                        user.setEmail(email);
                        user.setPassword(email);
                        user.setFirstName(firstName);
                        user.setLastName(lastName);

                        userService.save(new UserDTO(user));

                        authService.externalServiceLogin(request, response, user);
                        response.sendRedirect("http://localhost:4200/home");
                    }


                }) );

        http.securityContext((context) -> context.securityContextRepository(securityRepository));

        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filterAuthentication, UsernamePasswordAuthenticationFilter.class);

        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
