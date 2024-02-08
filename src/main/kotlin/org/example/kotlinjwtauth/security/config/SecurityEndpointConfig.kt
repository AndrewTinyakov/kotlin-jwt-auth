package org.example.kotlinjwtauth.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
@ConfigurationProperties(prefix = "security.endpoints")
@Order(1)
data class SecurityEndpointConfig (
    var unauthorized: List<String> = listOf(),
    var authorized: List<String> = listOf(),
    var optionallyAuthorized: List<String> = listOf()
)
