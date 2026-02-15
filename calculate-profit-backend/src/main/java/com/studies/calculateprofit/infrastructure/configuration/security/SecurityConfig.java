package com.studies.calculateprofit.infrastructure.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.calculateprofit.adapter.in.controller.error.ApiErrorResponse;
import com.studies.calculateprofit.adapter.in.controller.error.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwtAuthenticationFilter,
                                           ObjectMapper objectMapper,
                                           HandlerMappingIntrospector introspector) throws Exception {

        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector).servletPath("/");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                mvc.pattern("/v3/api-docs/**"),
                                mvc.pattern("/swagger-ui/**"),
                                mvc.pattern("/swagger-ui.html"),
                                mvc.pattern("/actuator/health"),
                                mvc.pattern("/h2-console/**")
                        ).permitAll()
                        .requestMatchers(mvc.pattern("/api/auth/**")).permitAll()
                        .requestMatchers(mvc.pattern("/api/**")).authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                writeUnauthorized(response, objectMapper,
                                        "Authentication is required to access this resource.")
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeUnauthorized(response, objectMapper,
                                        "You are not authorized to access this resource.")
                        )
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Allow H2 console to render in a frame during development
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    private void writeUnauthorized(HttpServletResponse response,
                                   ObjectMapper objectMapper,
                                   String message) throws java.io.IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String correlationId = UUID.randomUUID().toString();
        response.setHeader("X-Correlation-Id", correlationId);

        ApiErrorResponse body = new ApiErrorResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                ErrorCode.UNAUTHORIZED,
                message,
                correlationId
        );

        objectMapper.writeValue(response.getWriter(), body);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        var user = User.withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}