package com.toyProject7.karrot

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
class KarrotApplication {
    @PostConstruct
    fun verifyEnvVariables() {
        if (System.getenv("GOOGLE_CLIENT_ID") == null) {
            println("GOOGLE_CLIENT_ID is not set")
        } else {
            println("GOOGLE_CLIENT_ID is set")
        }
        // Repeat for other environment variables
        if (System.getenv("NAVER_CLIENT_ID") == null) {
            println("NAVER_CLIENT_ID is not set")
        } else {
            println("NAVER_CLIENT_ID is set")
        }
        if (System.getenv("KAKAO_CLIENT_ID") == null) {
            println("KAKAO_CLIENT_ID is not set")
        } else {
            println("KAKAO_CLIENT_ID is set")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<KarrotApplication>(*args)
}
