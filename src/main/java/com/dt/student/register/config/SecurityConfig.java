package com.dt.student.register.config;

import com.dt.student.register.authentication.filter.CustomCorsFilter;
import com.dt.student.register.authentication.filter.GatewayOnlyFilter;
import com.dt.student.register.authentication.filter.JwtRequestFilter;
import com.dt.student.register.mapper.primary.auth.AuthMapper;
import com.dt.student.register.mapper.primary.user.PermissionMapper;
import com.dt.student.register.mapper.primary.user.RoleMapper;
import com.dt.student.register.model.users.CustomUserDetails;
import com.dt.student.register.model.users.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.LinkedHashSet;

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(
            AuthMapper authMapper,
            RoleMapper roleMapper,
            PermissionMapper permissionMapper
    ) {
        return username -> {
            User user = authMapper.findUserDetails(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            LinkedHashSet<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();
            roleMapper.getRoleNamesByUserId(user.getId()).stream()
                    .filter(role -> role != null && !role.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
            permissionMapper.getPermissionCodesByUserId(user.getId()).stream()
                    .filter(permission -> permission != null && !permission.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);

            return new CustomUserDetails(
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    authorities
            );
        };
    }
}
