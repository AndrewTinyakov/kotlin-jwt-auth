package org.example.kotlinjwtauth.security.exception.handler

import lombok.extern.slf4j.Slf4j
@ControllerAdvice
@Slf4j
class SecurityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(): ResponseEntity<*> {
        log.error("ForbiddenException")

        val body: MutableMap<String, Any> = java.util.LinkedHashMap()
        body["error"] = "Forbidden"
        body["timestamp"] = java.time.LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, org.springframework.http.HttpStatus.OK)
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExpiredRefreshTokenException::class)
    fun handleExpiredRefreshTokenException(): ResponseEntity<*> {
        log.warn("ExpiredRefreshTokenException")

        val body: MutableMap<String, Any> = java.util.LinkedHashMap()
        body["error"] = "Refresh token was expired"
        body["timestamp"] = java.time.LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, org.springframework.http.HttpStatus.OK)
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidRefreshTokenException::class)
    fun handleInvalidRefreshTokenException(): ResponseEntity<*> {
        log.warn("InvalidRefreshTokenException")

        val body: MutableMap<String, Any> = java.util.LinkedHashMap()
        body["error"] = "Refresh token is invalid"
        body["timestamp"] = java.time.LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, org.springframework.http.HttpStatus.OK)
    }
}
