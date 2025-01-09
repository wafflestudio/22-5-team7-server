package com.toyProject7.karrot

import com.toyProject7.karrot.security.JwtAuthenticationFilter
import com.toyProject7.karrot.socialLogin.handler.CustomAuthenticationSuccessHandler
import com.toyProject7.karrot.socialLogin.service.SocialLoginUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val socialLoginUserService: SocialLoginUserService,
    private val customAuthenticationSuccessHandler: CustomAuthenticationSuccessHandler,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { registry ->
                registry
                    .requestMatchers(
                        "/api/auth/**",
                        "oauth2/**",
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2login ->
                oauth2login
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(socialLoginUserService)
                    }
                    .successHandler(customAuthenticationSuccessHandler)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}
