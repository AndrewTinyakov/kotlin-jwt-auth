package org.example.kotlinjwtauth.security.token.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseCookie

interface TokenUtils {
    fun generateJwtCookieByUserId(userId: Long): ResponseCookie?

    fun generateRefreshTokenCookie(tokenId: Long): ResponseCookie?

    fun getAccessTokenFromCookies(request: HttpServletRequest?): String?

    fun getRefreshTokenFromCookies(request: HttpServletRequest?): String?

    val cleanAccessTokenCookie: ResponseCookie?

    val cleanRefreshTokenCookie: ResponseCookie?

    fun getIdFromAccessToken(token: String?): String?

    fun getIdFromRefreshToken(token: String?): String?

    fun validateJwtAccessToken(authToken: String?): Boolean

    fun validateJwtRefreshToken(refreshToken: String?): Boolean
}
