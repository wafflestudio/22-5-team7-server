package com.toyProject7.karrot.security

object SecurityConstants {
    val PUBLIC_PATHS =
        arrayOf(
            "/api/auth/**",
            "/oauth2/**",
            "/auth/**",
            "/login/oauth2/**",
            "/api/test",
            "/ws/**",
        )
}
