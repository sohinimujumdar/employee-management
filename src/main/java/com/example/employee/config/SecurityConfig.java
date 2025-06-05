package com.example.employee.config;

import com.example.employee.service.UserService;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//when the user register the function encrypts password securely
    @Bean
    public DaoAuthenticationProvider authProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    // allows and disallows the usages

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                        .anyRequest().authenticated()
                )
                .saml2Login(saml -> saml
                        .authenticationConverter(authentication -> {
                            Saml2Authentication samlAuth = (Saml2Authentication) authentication;
                            Saml2AuthenticatedPrincipal principal = (Saml2AuthenticatedPrincipal) samlAuth.getPrincipal();

                            // Get role from SAML attribute
                            String role = (String) principal.getFirstAttribute("role");

                            // Convert to Spring Security authority
                            List<GrantedAuthority> authorities = Collections.singletonList(
                                    new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                            );

                            return new Saml2Authentication(principal, samlAuth.getSaml2Response(), authorities);
                        })
                );

        return http.build();
    }

}