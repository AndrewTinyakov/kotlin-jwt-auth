package org.example.kotlinjwtauth.security.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.example.kotlinjwtauth.security.payload.request.LoginRequest
import org.example.kotlinjwtauth.security.payload.request.SignUpRequest
import org.example.kotlinjwtauth.security.service.auth.LoginService
import org.example.kotlinjwtauth.security.service.auth.RegistrationService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val loginService: LoginService,
    private val registrationService: RegistrationService
) {
    @PostMapping("/registration")
    fun registerUser(@RequestBody signUpRequest: @Valid SignUpRequest?) {
        log.debug("Register request: username={}", signUpRequest!!.username)

        registrationService.registration(signUpRequest)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: @Valid LoginRequest?): ResponseEntity<*> {
        log.debug("Login request: username={}", loginRequest!!.username)

        val loginResponse = loginService.login(loginRequest)
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, loginResponse.getAccessTokenCookie.toString())
            .header(HttpHeaders.SET_COOKIE, loginResponse.getRefreshTokenCookie.toString())
            .body<Any>(null)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(request: HttpServletRequest?): ResponseEntity<*> {
        log.debug("Refresh token request")

        val response = loginService.refreshToken(request)
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, response.getAccessTokenCookie.toString())
            .header(HttpHeaders.SET_COOKIE, response.getRefreshTokenCookie.toString())
            .body<Any>(null)
    }

    @PostMapping("/logout")
    fun logoutUser(httpServletRequest: HttpServletRequest?): ResponseEntity<String> {
        log.debug("Logout user request")

        val response = loginService.logout(httpServletRequest)
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, response.getAccessTokenCookie.toString())
            .header(HttpHeaders.SET_COOKIE, response.getRefreshTokenCookie.toString())
            .body(null)
    }
}
