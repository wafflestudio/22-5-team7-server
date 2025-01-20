package com.toyProject7.karrot.security

import com.toyProject7.karrot.user.UserAccessTokenUtil
import com.toyProject7.karrot.user.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userService: UserService,
) : OncePerRequestFilter() {
    private val excludedPaths = SecurityConstants.PUBLIC_PATHS.map { AntPathRequestMatcher(it) }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        // Skip filtering for excluded endpoints
        if (isExcluded(request)) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            try {
                // Validate the token
                if (UserAccessTokenUtil.validateToken(token)) {
                    // Get user ID from token
                    val userId = UserAccessTokenUtil.getUserIdFromToken(token)

                    // Load user details
                    val userDetails = userService.getUserEntityById(userId)

                    // Create authentication token
                    val authentication =
                        UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                        )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                    // Set the authentication in the context
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: Exception) {
                // Handle exceptions (e.g., log them)
                println("Failed to authenticate user: ${e.message}")
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun isExcluded(request: HttpServletRequest): Boolean {
        return excludedPaths.any { it.matches(request) }
    }
}
