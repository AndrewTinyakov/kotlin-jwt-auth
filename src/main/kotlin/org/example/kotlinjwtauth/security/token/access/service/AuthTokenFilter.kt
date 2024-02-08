package org.example.kotlinjwtauth.security.token.access.service

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.kotlinjwtauth.security.config.SecurityEndpointConfig
import org.example.kotlinjwtauth.security.constraint.exception.AuthExceptionMessageConstants.BAD_CREDENTIALS
import org.example.kotlinjwtauth.security.exception.handler.AuthEntryPointJwt
import org.example.kotlinjwtauth.security.token.dto.EndpointType
import org.example.kotlinjwtauth.security.token.service.TokenUtils
import org.example.kotlinjwtauth.security.user.service.CustomUserDetailsService
import org.springframework.lang.NonNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class AuthTokenFilter(
    private val tokenUtils: TokenUtils,
    private val userDetailsService: CustomUserDetailsService,
    private val antPathMatcher: AntPathMatcher,
    private val securityEndpointConfig: SecurityEndpointConfig,
    private val authenticationEntryPoint: AuthEntryPointJwt
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain
    ) {
        try {
            val jwt: String? = tokenUtils.getAccessTokenFromCookies(request)
            val endpointType: EndpointType = getEndpointType(request)
            val valid: Boolean = tokenUtils.validateJwtAccessToken(jwt)

            when (endpointType) {
                EndpointType.OPTIONALLY_AUTHORIZED -> {
                    if (!jwt.isNullOrBlank()) {
                        if (!valid) {
                            throw BadCredentialsException(BAD_CREDENTIALS)
                        } else {
                            authorize(jwt, request)
                        }
                    }
                }

                EndpointType.AUTHORIZED -> {
                    if (!valid) {
                        throw BadCredentialsException(BAD_CREDENTIALS)
                    }
                    authorize(jwt, request)
                }

                else -> {}
            }
        } catch (ex: BadCredentialsException) {
            authenticationEntryPoint.commence(request, response, ex)

            return
        }

        filterChain.doFilter(request, response)
    }

    private fun authorize(jwt: String?, request: HttpServletRequest) {
        val id = tokenUtils.getIdFromAccessToken(jwt!!)
        val userDetails = userDetailsService.loadUserById(id)

        val authentication = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        )
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun getEndpointType(request: HttpServletRequest): EndpointType {
        val requestURI = request.requestURI

        if (securityEndpointConfig.unauthorized.any { pattern: String? ->
                antPathMatcher.match(pattern!!, requestURI)
            }) {
            return EndpointType.UNAUTHORIZED
        }
        if (securityEndpointConfig.authorized.any { pattern: String? ->
                antPathMatcher.match(pattern!!, requestURI)
            }) {
            return EndpointType.AUTHORIZED
        }
        if (securityEndpointConfig.optionallyAuthorized.any { pattern: String? ->
                antPathMatcher.match(pattern!!, requestURI)
            }) {
            return EndpointType.OPTIONALLY_AUTHORIZED
        }
        return EndpointType.AUTHORIZED
    }
}
