package org.example.kotlinjwtauth.security.exception.handler

import org.example.kotlinjwtauth.security.exception.ForbiddenException
import org.example.kotlinjwtauth.security.exception.registration.TakenEmailException
import org.example.kotlinjwtauth.security.exception.registration.TakenUsernameAndEmailException
import org.example.kotlinjwtauth.security.exception.registration.TakenUsernameException
import org.example.kotlinjwtauth.security.token.refresh.exception.ExpiredRefreshTokenException
import org.example.kotlinjwtauth.security.token.refresh.exception.InvalidRefreshTokenException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class SecurityExceptionHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbiddenException(): ResponseEntity<*> {
        log.error("ForbiddenException")

        val body: MutableMap<String, Any> = linkedMapOf()
        body["error"] = "Forbidden"
        body["timestamp"] = LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, HttpStatus.OK)
    }

    @ExceptionHandler(ExpiredRefreshTokenException::class)
    fun handleExpiredRefreshTokenException(): ResponseEntity<*> {
        log.warn("ExpiredRefreshTokenException")

        val body: MutableMap<String, Any> = linkedMapOf()
        body["error"] = "Refresh token was expired"
        body["timestamp"] = LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, HttpStatus.OK)
    }

    @ExceptionHandler(InvalidRefreshTokenException::class)
    fun handleInvalidRefreshTokenException(): ResponseEntity<*> {
        log.warn("InvalidRefreshTokenException")

        val body: MutableMap<String, Any> = LinkedHashMap()
        body["error"] = "Refresh token is invalid"
        body["timestamp"] = LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, HttpStatus.OK)
    }

    @ExceptionHandler(TakenUsernameAndEmailException::class)
    fun handleTakenUsernameAndEmailException(): ResponseEntity<*> {
        log.warn("TakenUsernameAndEmailException")

        val body: MutableMap<String, Any> = linkedMapOf()
        body["error"] = "Username and email are taken"
        body["type"] = "both"
        body["timestamp"] = LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, HttpStatus.OK)
    }

    @ExceptionHandler(TakenEmailException::class)
    fun handleTakenEmailException(): ResponseEntity<*> {
        log.warn("TakenEmailException")

        val body: MutableMap<String, Any> = linkedMapOf()
        body["error"] = "Email is taken "
        body["type"] = "email"
        body["timestamp"] = LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, HttpStatus.OK)
    }

    @ExceptionHandler(TakenUsernameException::class)
    fun handleTakenUsernameException(): ResponseEntity<*> {
        log.warn("TakenUsernameException")

        val body: MutableMap<String, Any> = linkedMapOf()
        body["error"] = "Username is taken"
        body["type"] = "username"
        body["timestamp"] = LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, HttpStatus.OK)
    }


}
