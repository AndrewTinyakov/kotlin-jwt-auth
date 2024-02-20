package org.example.kotlinjwtauth.security.config

import org.example.kotlinjwtauth.security.exception.handler.AuthEntryPointJwt
import org.example.kotlinjwtauth.security.token.access.service.AuthTokenFilter
import org.example.kotlinjwtauth.security.token.service.TokenUtils
import org.example.kotlinjwtauth.security.user.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.util.AntPathMatcher

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Order(2)
class SecurityConfig(
    private val userDetailsService: CustomUserDetailsService,
    private val tokenUtils: TokenUtils,
    private val antPathMatcher: AntPathMatcher,
    private val securityEndpointConfig: SecurityEndpointConfig,
    private val unauthorizedHandler: AuthEntryPointJwt,
) {
    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        return AuthTokenFilter(
            tokenUtils,
            userDetailsService,
            antPathMatcher,
            securityEndpointConfig,
            unauthorizedHandler
        )
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()

        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())

        return authProvider
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return ProviderManager(authenticationProvider())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { requests ->
            requests
                .requestMatchers(*securityEndpointConfig.unauthorized.toTypedArray()).permitAll()
                .requestMatchers(*securityEndpointConfig.authorized.toTypedArray()).authenticated()
                .requestMatchers(HttpMethod.GET, *securityEndpointConfig.optionallyAuthorized.toTypedArray())
                .permitAll()
                .anyRequest()
                .authenticated()
        }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .csrf { csrfConfigurer ->
                csrfConfigurer.disable()
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { configurer ->
                configurer.authenticationEntryPoint(unauthorizedHandler)
            }
            .build()
    }
}
