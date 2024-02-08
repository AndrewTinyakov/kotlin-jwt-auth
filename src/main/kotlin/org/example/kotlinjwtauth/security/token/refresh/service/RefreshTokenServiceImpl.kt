package org.example.kotlinjwtauth.security.token.refresh.service

import org.example.kotlinjwtauth.security.service.utils.HashService
import org.example.kotlinjwtauth.security.token.refresh.dao.repository.RefreshTokenRepository
import org.example.kotlinjwtauth.security.token.refresh.model.RefreshToken
import org.example.kotlinjwtauth.security.token.service.TokenUtils
import org.example.kotlinjwtauth.user.model.User
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class RefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val hashService: HashService,
    private val tokenUtils: TokenUtils
) : RefreshTokenService {
    @Transactional
    fun saveToken(refreshToken: RefreshToken): RefreshToken {
        return refreshTokenRepository.save(refreshToken)
    }

    override fun updateTokenValueAndEncode(value: String, id: Long) {
        val encode = hashService.hashStringWithSHA256(value)
        refreshTokenRepository.updateHashedTokenById(encode, id)
    }

    override fun findById(id: String): Optional<RefreshToken> {
        return refreshTokenRepository.findById(id.toLong())
    }

    override fun existsById(id: Long): Boolean {
        return refreshTokenRepository.existsById(id)
    }

    @Transactional
    override fun createRefreshTokenByUserId(userId: Long): ResponseCookie {
        val user: User = User()
        user.id = userId
        val refreshToken = RefreshToken(
            user
        )
        val tokenWithId = saveToken(refreshToken)
        val responseCookie = tokenUtils.generateRefreshTokenCookie(tokenWithId.id!!)
        tokenWithId.hashedToken = hashService.hashStringWithSHA256(responseCookie.value)
        saveToken(tokenWithId)

        return responseCookie
    }


    @Transactional
    override fun deleteTokenById(id: Long) {
        refreshTokenRepository.deleteById(id)
    }
}
