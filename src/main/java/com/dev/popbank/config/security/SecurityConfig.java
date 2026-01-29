package com.dev.popbank.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Rota PÚBLICA 
                        // Apenas login e criar conta comum
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/wallets/ping").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Rota de ADMINISTRAÇÃO 
                        .requestMatchers(HttpMethod.POST, "/api/auth/register-admin").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/contas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/wallets/{id}/deposit").hasRole("ADMIN")

                        // TRANSAÇÕES
                        .requestMatchers(HttpMethod.GET, "/api/transactions").hasRole("ADMIN") 
                        .requestMatchers(HttpMethod.DELETE, "/api/transactions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/transactions/*/undo").hasRole("ADMIN")
                        
                        // USUÁRIO LOGADO
                        .requestMatchers(HttpMethod.POST, "/api/transactions").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/transactions/{id}").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}