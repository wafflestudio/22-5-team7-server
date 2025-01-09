package com.toyProject7.karrot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
@EntityScan(basePackages = ["com.toyProject7.karrot.user.persistence"])
class KarrotApplication

fun main(args: Array<String>) {
    runApplication<KarrotApplication>(*args)
}
