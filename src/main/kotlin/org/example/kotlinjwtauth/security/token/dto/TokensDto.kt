package org.example.kotlinjwtauth.security.token.dto

import org.springframework.http.ResponseCookie

@JvmRecord
data class TokensDto(
    val getAccessTokenCookie: ResponseCookie,
    val getRefreshTokenCookie: ResponseCookie
)
