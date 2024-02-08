package org.example.kotlinjwtauth.security.token.access.service

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.kotlinjwtauth.security.config.SecurityEndpointConfig
import org.example.kotlinjwtauth.security.token.dto.EndpointType
import org.example.kotlinjwtauth.security.token.service.TokenUtils
import org.example.kotlinjwtauth.security.user.service.CustomUserDetailsService
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
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
    private val securityEndpointConfig: SecurityEndpointConfig
) : OncePerRequestFilter() {
    private val mapper = ObjectMapper()

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain
    ) {
        try {
            val jwt = parseJwt(request)
            val endpointType = getEndpointType(request)
            val valid = tokenUtils.validateJwtAccessToken(jwt!!)

            when (endpointType) {
                EndpointType.OPTIONALLY_AUTHORIZED -> {
                    if (jwt != null && !jwt.isBlank()) {
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
            }
        } catch (ex: BadCredentialsException) {
            SecurityContextHolder.clearContext()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = HttpServletResponse.SC_OK

            val body: MutableMap<String, Any?> = HashMap()
            body["status"] = HttpServletResponse.SC_UNAUTHORIZED
            body["error"] = ex.message

            mapper.writeValue(response.outputStream, body)

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

    private fun parseJwt(request: HttpServletRequest): String? {
        return tokenUtils.getAccessTokenFromCookies(request)
    }

    private fun getEndpointType(request: HttpServletRequest): EndpointType {
        val requestURI = request.requestURI

        if (securityEndpointConfig.unauthorized.stream()
                .anyMatch { pattern: String? -> antPathMatcher.match(pattern!!, requestURI) }
        ) {
            return EndpointType.UNAUTHORIZED
        }
        if (securityEndpointConfig.authorized.stream()
                .anyMatch { pattern: String? -> antPathMatcher.match(pattern!!, requestURI) }
        ) {
            return EndpointType.AUTHORIZED
        }
        if (securityEndpointConfig.optionallyAuthorized.stream()
                .anyMatch { pattern: String? ->
                    antPathMatcher.match(pattern!!, requestURI) ||
                            HttpMethod.GET.matches(request.method)
                }
        ) {
            return EndpointType.OPTIONALLY_AUTHORIZED
        }
        return EndpointType.AUTHORIZED
    }
}
