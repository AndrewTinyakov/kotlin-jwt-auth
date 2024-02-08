package org.example.kotlinjwtauth.security.token.refresh.service

import org.example.kotlinjwtauth.security.token.refresh.model.RefreshToken
import org.springframework.http.ResponseCookie
import java.util.*

interface RefreshTokenService {
    fun updateTokenValueAndEncode(value: String?, id: Long?)

    fun findById(id: String?): Optional<RefreshToken?>?

    fun existsById(id: Long?): Boolean

    fun createRefreshTokenByUserId(userId: Long): ResponseCookie?

    fun deleteTokenById(id: Long)
}
