package com.example.final_project.config;

import com.example.final_project.infrastructure.service.MongoUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager customAuthenticationManager(
            HttpSecurity http,
            MongoUserDetailsService service,
            PasswordEncoder encoder
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(service)
                .passwordEncoder(encoder)
                .and()
                .build();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        return security
                .cors()
                .disable()
                .csrf()
                .disable()
                .authorizeRequests(req -> req.antMatchers("/user").permitAll())
                .authorizeRequests(req -> req.antMatchers("/**").authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }


}
