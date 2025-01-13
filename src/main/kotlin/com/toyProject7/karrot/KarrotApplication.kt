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
        if (System.getenv("JWT_SECRET_KEY") == null) {
            println("JWT_SECRET_KEY is not set")
        } else {
            println("JWT_SECRET_KEY is set")
        }
        if (System.getenv("AWS_ACCESS_KEY_ID") == null) {
            println("AWS_ACCESS_KEY_ID is not set")
        } else {
            println("AWS_ACCESS_KEY_ID is set")
        }
        if (System.getenv("AWS_SECRET_ACCESS_KEY") == null) {
            println("AWS_SECRET_ACCESS_KEY is not set")
        } else {
            println("AWS_SECRET_ACCESS_KEY is set")
        }
        if (System.getenv("AWS_S3_BUCKET") == null) {
            println("AWS_S3_BUCKET is not set")
        } else {
            println("AWS_S3_BUCKET is set")
        }
        if (System.getenv("AWS_REGION") == null) {
            println("AWS_REGION is not set")
        } else {
            println("AWS_REGION is set")
        }
        if (System.getenv("GOOGLE_CLI_SECRET") == null) {
            println("GOOGLE_CLI_SECRET is not set")
        } else {
            println("GOOGLE_CLI_SECRET is set")
        }
        if (System.getenv("NAVER_CLI_SECRET") == null) {
            println("NAVER_CLI_SECRET is not set")
        } else {
            println("NAVER_CLI_SECRET is set")
        }
        if (System.getenv("KAKAO_CLI_SECRET") == null) {
            println("KAKAO_CLI_SECRET is not set")
        } else {
            println("KAKAO_CLI_SECRET is set")
        }
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
