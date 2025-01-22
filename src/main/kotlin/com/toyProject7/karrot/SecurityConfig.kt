package com.toyProject7.karrot

import com.toyProject7.karrot.security.JwtAuthenticationFilter
import com.toyProject7.karrot.security.OAuth2AuthenticationClearingFilter
import com.toyProject7.karrot.security.SecurityConstants
import com.toyProject7.karrot.socialLogin.handler.CustomAuthenticationSuccessHandler
import com.toyProject7.karrot.socialLogin.service.SocialLoginUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

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
            .cors { cors ->
                cors.configurationSource(corsConfigurationSource())
            }
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { registry ->
                registry
                    .requestMatchers(
                        *SecurityConstants.PUBLIC_PATHS,
                        "/ws/**",
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            // Disable form login
            .formLogin { formLogin -> formLogin.disable() }
            // Configure exception handling
            .exceptionHandling { exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            .oauth2Login { oauth2login ->
                oauth2login
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(socialLoginUserService)
                    }
                    .successHandler(customAuthenticationSuccessHandler)
            }
            .addFilterBefore(OAuth2AuthenticationClearingFilter(), JwtAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins =
            listOf(
                "https://toykarrot.shop",
                "http://localhost:5173",
            )
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
