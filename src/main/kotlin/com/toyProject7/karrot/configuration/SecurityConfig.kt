package com.toyProject7.karrot.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { registry ->
                registry
                    .requestMatchers("/").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { customizer -> customizer }
            .formLogin { customizer -> customizer }

        return http.build()
    }
}
