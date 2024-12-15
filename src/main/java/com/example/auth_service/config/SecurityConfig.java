package com.example.auth_service.config;

import com.example.auth_service.security.AuthenticationFilter;
import com.example.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String[] WHITE_LIST = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return userService::findByEmail;
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
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationFilter authFilter,
            AuthenticationEntryPoint entryPoint,
            LogoutHandler logoutHandler
    ) throws Exception {
        http
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers("/users/**").authenticated()

                                .requestMatchers("/api/v1/super-admin").hasRole("SUPER_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/super-admin").hasAuthority("super-admin:read")
                                .requestMatchers(HttpMethod.POST, "/api/v1/super-admin").hasAuthority("super-admin:insert")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/super-admin").hasAuthority("super-admin:update")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/super-admin").hasAuthority("super-admin:delete")

                                .requestMatchers("/api/v1/admin").hasAnyRole("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/admin").hasAnyAuthority("super-admin:read", "admin:read")
                                .requestMatchers(HttpMethod.POST, "/api/v1/admin").hasAnyAuthority("super-admin:insert", "admin:insert")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/admin").hasAnyAuthority("super-admin:update", "admin:update")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/admin").hasAnyAuthority("super-admin:delete", "admin:delete")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(
//                        exception -> exception.authenticationEntryPoint(entryPoint))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(
                                        (request, response, authentication) ->
                                                SecurityContextHolder.clearContext()
                                )
                                .logoutSuccessUrl("/api/v1/auth/logout"));

        return http.build();
    }

}