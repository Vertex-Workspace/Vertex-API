package com.vertex.vertex.security;

import com.vertex.vertex.user.model.DTO.UserDTO;
import com.vertex.vertex.user.model.DTO.UserLoginDTO;
import com.vertex.vertex.user.model.entity.User;
import com.vertex.vertex.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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
        .oauth2Login(httpOauth2 -> httpOauth2.successHandler(authService::initExternalServiceLogin));

        http.securityContext((context) -> context.securityContextRepository(securityRepository));

        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filterAuthentication, UsernamePasswordAuthenticationFilter.class);

        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
