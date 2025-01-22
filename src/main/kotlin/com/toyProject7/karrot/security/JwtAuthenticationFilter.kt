package com.toyProject7.karrot.security

import com.toyProject7.karrot.user.UserAccessTokenUtil
import com.toyProject7.karrot.user.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
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
                    logger.debug("User ID extracted from token: $userId")

                    // Load user details
                    val userDetails = userService.loadUserPrincipalById(userId)

                    // Create authentication token with a default authority
                    val authentication =
                        UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities,
                        )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                    // Set the authentication in the context
                    SecurityContextHolder.getContext().authentication = authentication
                } else {
                    // Invalid token scenario
                    logger.warn("Invalid JWT token: $token")
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("Invalid JWT Token")
                    return
                }
            } catch (e: Exception) {
                // Handle exceptions
                logger.error("Failed to authenticate user: ${e.message}", e)
                response.status = HttpServletResponse.SC_UNAUTHORIZED // Set 401 status
                return
            }
        } else if (SecurityContextHolder.getContext().authentication is OAuth2AuthenticationToken) {
            // Fallback: Handle cases where OAuth2AuthenticationToken is still present
            logger.debug("OAuth2AuthenticationToken detected; forcing JWT authentication fallback")

            // Force re-authentication based on the token in the Authorization header
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                val token = authHeader.substring(7)
                if (UserAccessTokenUtil.validateToken(token)) {
                    val userId = UserAccessTokenUtil.getUserIdFromToken(token)
                    val userDetails = userService.loadUserPrincipalById(userId)

                    val authentication =
                        UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities,
                        )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                    logger.debug("Re-authentication completed for user: $userId")
                }
            }
        }

        // Continue the filter chain regardless of authentication
        filterChain.doFilter(request, response)
    }

    private fun isExcluded(request: HttpServletRequest): Boolean {
        return excludedPaths.any { it.matches(request) }
    }
}
