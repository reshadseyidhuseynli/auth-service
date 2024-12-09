package com.example.auth_service.config;

import com.example.auth_service.security.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {

        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationFilter authFilter,
                                                   AuthenticationEntryPoint entryPoint)
            throws Exception {
        http
//                .authorizeHttpRequests(
//                        request -> request
//                                .requestMatchers("/api/v1/super-admin").hasRole("SUPER_ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/v1/super-admin").hasAuthority("super-admin:read")
//                                .requestMatchers(HttpMethod.POST, "/api/v1/super-admin").hasAuthority("super-admin:insert")
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/super-admin").hasAuthority("super-admin:update")
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/super-admin").hasAuthority("super-admin:delete")
//
//                                .requestMatchers("/api/v1/admin").hasAnyRole("SUPER_ADMIN", "ADMIN")
//                                .requestMatchers(HttpMethod.GET,"/api/v1/admin").hasAnyAuthority("super-admin:read", "admin:read")
//                                .requestMatchers(HttpMethod.POST,"/api/v1/admin").hasAnyAuthority("super-admin:insert", "admin:insert")
//                                .requestMatchers(HttpMethod.PUT,"/api/v1/admin").hasAnyAuthority("super-admin:update", "admin:update")
//                                .requestMatchers(HttpMethod.DELETE,"/api/v1/admin").hasAnyAuthority("super-admin:delete", "admin:delete"))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(entryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}