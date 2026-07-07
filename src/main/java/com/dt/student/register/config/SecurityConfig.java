package com.dt.student.register.config;

import com.dt.student.register.authentication.filter.CustomCorsFilter;
import com.dt.student.register.authentication.filter.GatewayOnlyFilter;
import com.dt.student.register.authentication.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomCorsFilter customCorsFilter;

    public SecurityConfig(
            JwtRequestFilter jwtRequestFilter,
            CustomCorsFilter customCorsFilter
    ) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.customCorsFilter = customCorsFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, GatewayOnlyFilter gatewayOnlyFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/api/auth/**",
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/actuator/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // ✅ Filters are now properly ordered
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(gatewayOnlyFilter, JwtRequestFilter.class)
                .addFilterBefore(customCorsFilter, GatewayOnlyFilter.class);

        return http.build();
    }

    @Bean
    public GatewayOnlyFilter gatewayOnlyFilter() {
        return new GatewayOnlyFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
