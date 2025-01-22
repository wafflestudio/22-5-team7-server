package com.toyProject7.karrot.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter

class OAuth2AuthenticationClearingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val existingAuth = SecurityContextHolder.getContext().authentication
        if (existingAuth is OAuth2AuthenticationToken) {
            logger.debug("Clearing OAuth2AuthenticationToken for request: ${request.requestURI}")
            SecurityContextHolder.clearContext()
        }
        filterChain.doFilter(request, response)
    }
}
