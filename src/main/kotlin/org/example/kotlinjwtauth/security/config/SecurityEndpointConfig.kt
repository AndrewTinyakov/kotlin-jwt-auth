package org.example.kotlinjwtauth.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
@ConfigurationProperties(prefix = "security.endpoints")
@Order(1)
class SecurityEndpointConfig {
    constructor(unauthorized: List<String>?, authorized: List<String>?, optionallyAuthorized: List<String>?) {
        this.unauthorized = unauthorized
        this.authorized = authorized
        this.optionallyAuthorized = optionallyAuthorized
    }

    constructor()

    var unauthorized: List<String>? = null
        private set

    var authorized: List<String>? = null
        private set

    var optionallyAuthorized: List<String>? = null
        private set
}
