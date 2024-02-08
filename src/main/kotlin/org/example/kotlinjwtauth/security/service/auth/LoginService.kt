package org.example.kotlinjwtauth.security.service.auth

import jakarta.servlet.http.HttpServletRequest
import org.example.kotlinjwtauth.security.payload.request.LoginRequest
import org.example.kotlinjwtauth.security.token.dto.TokensDto

interface LoginService {
    fun login(loginRequest: LoginRequest): TokensDto

    fun logout(httpServletRequest: HttpServletRequest): TokensDto

    fun refreshToken(request: HttpServletRequest): TokensDto
}
