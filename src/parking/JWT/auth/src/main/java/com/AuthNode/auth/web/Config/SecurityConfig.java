package com.AuthNode.auth.web.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private static final String ADMIN = "admin";
    private static final String USER = "user";

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/space/all").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.GET, "/api/space/all/pageable").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/space/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.POST, "/api/space/add").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.PUT, "/api/space/update/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.DELETE, "/api/space/delete/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.GET, "/api/reserva/all").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.GET, "/api/reserva/all/pageable").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/reserva/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.POST, "/api/reserva/add").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.PUT, "/api/reserva/update/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.DELETE, "/api/reserva/delete/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.GET, "/api/comment/all").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.GET, "/api/comment/all/pageable").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/comment/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.POST, "/api/comment/add").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.PUT, "/api/comment/update/{id}").hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/api/comment/delete/{id}").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/parking/all").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.GET, "/api/parking/all/pageable").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/parking/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.POST, "/api/parking/add").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.PUT, "/api/parking/update/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.DELETE, "/api/parking/delete/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.GET, "/api/vehiculo/all").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.GET, "/api/vehiculo/all/pageable").hasRole(ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/vehiculo/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.POST, "/api/vehiculo/add").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.PUT, "/api/vehiculo/update/{id}").hasAnyRole(ADMIN, USER)
                .requestMatchers(HttpMethod.DELETE, "/api/vehiculo/delete/{id}").hasAnyRole(ADMIN, USER)
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "http://localhost:4200",
            "http://localhost"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}