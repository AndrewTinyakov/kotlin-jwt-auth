package org.example.kotlinjwtauth.security.service.auth

import jakarta.servlet.http.HttpServletRequest
import org.example.kotlinjwtauth.global.exception.BadRequestException
import org.example.kotlinjwtauth.security.payload.request.LoginRequest
import org.example.kotlinjwtauth.security.service.utils.HashService
import org.example.kotlinjwtauth.security.token.dto.TokensDto
import org.example.kotlinjwtauth.security.token.refresh.exception.InvalidRefreshTokenException
import org.example.kotlinjwtauth.security.token.refresh.service.RefreshTokenService
import org.example.kotlinjwtauth.security.token.service.TokenUtils
import org.example.kotlinjwtauth.security.user.model.UserDetailsImpl
import org.example.kotlinjwtauth.user.service.UserService
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class LoginServiceImpl(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val tokenUtils: TokenUtils,
    private val refreshTokenService: RefreshTokenService,
    private val hashService: HashService
) : LoginService {
    @Transactional(noRollbackFor = [BadCredentialsException::class])
    override fun login(loginRequest: LoginRequest): TokensDto {
        val user = userService.findUserByUsername(loginRequest.username)
            .orElseThrow<BadCredentialsException> { BadCredentialsException(BAD_CREDENTIALS) }

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )

        val userDetails = Objects.requireNonNull(authentication).principal as UserDetailsImpl

        val accessTokenCookie = tokenUtils.generateJwtCookieByUserId(userDetails.id)
        val refreshTokenCookie = refreshTokenService.createRefreshTokenByUserId(userDetails.id)

        log.debug("User logged in: id={}, username={}", user.getId(), user.getUsername())
        return TokensDto(
            accessTokenCookie,
            refreshTokenCookie
        )
    }

    @Transactional
    override fun logout(request: HttpServletRequest): TokensDto {
        val refreshToken = tokenUtils.getRefreshTokenFromCookies(request)
            ?: throw BadRequestException()
        val idFromRefreshToken: String
        try {
            idFromRefreshToken = tokenUtils.getIdFromRefreshToken(refreshToken)
        } catch (e: IllegalArgumentException) {
            throw BadRequestException()
        }
        val id = idFromRefreshToken.toLong()
        if (!refreshToken.isBlank() && refreshTokenService.existsById(id)) {
            refreshTokenService.deleteTokenById(id)
        }

        val jwtAccessCookie: ResponseCookie = tokenUtils.cleanAccessTokenCookie
        val jwtRefreshCookie: ResponseCookie = tokenUtils.cleanRefreshTokenCookie

        log.debug("User logged out")
        return TokensDto(
            jwtAccessCookie,
            jwtRefreshCookie
        )
    }

    @Transactional
    override fun refreshToken(request: HttpServletRequest): TokensDto {
        val refreshToken = tokenUtils.getRefreshTokenFromCookies(request)

        if (refreshToken == null || refreshToken.isBlank()) {
            log.warn("Invalid refresh token")
            throw InvalidRefreshTokenException()
        }
        val valid = tokenUtils.validateJwtRefreshToken(refreshToken)
        if (!valid) {
            throw InvalidRefreshTokenException()
        }
        val idFromRefreshToken = tokenUtils.getIdFromRefreshToken(refreshToken)

        val token =
            refreshTokenService.findById(idFromRefreshToken)
                .orElseThrow { InvalidRefreshTokenException() }

        val matches = hashService.verifySHA256Hash(refreshToken, token.getHashedToken())
        if (!matches) {
            log.warn("Refresh token hash doesnt match")
            throw InvalidRefreshTokenException()
        }
        val newRefreshTokenCookie = tokenUtils.generateRefreshTokenCookie(token.getId())

        refreshTokenService.updateTokenValueAndEncode(newRefreshTokenCookie.value, token.getId())

        val accessTokenCookie = tokenUtils.generateJwtCookieByUserId(token.getUser().getId())

        //
        log.debug("Refresh token: userId={}, tokenId={}", token.getUser().getId(), token.getId())
        return TokensDto(
            accessTokenCookie,
            newRefreshTokenCookie
        )
    }
}

