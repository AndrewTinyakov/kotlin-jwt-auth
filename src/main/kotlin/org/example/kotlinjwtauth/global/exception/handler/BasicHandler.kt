package org.example.kotlinjwtauth.global.exception.handler

import org.example.kotlinjwtauth.global.exception.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class BasicHandler {

    val log: Logger = getLogger(this::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(): ResponseEntity<*> {
        log.warn("NotFoundException")

        val body: MutableMap<String, Any> = LinkedHashMap()
        body["error"] = "Not found"
        body["timestamp"] = LocalDateTime.now()

        return ResponseEntity<Map<String, Any>>(body, HttpStatus.OK)
    }
}
