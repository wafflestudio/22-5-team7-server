package com.toyProject7.karrot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests { registry ->
                registry.requestMatchers("/", "/login").permitAll()
                registry.anyRequest().authenticated()
            }
            .oauth2Login { oauth2login ->
                oauth2login
                    .loginPage("/login")
                    .successHandler { request, response, authentication ->
                        response.sendRedirect("/profile")
                    }
            }
            .build()
    }
}
