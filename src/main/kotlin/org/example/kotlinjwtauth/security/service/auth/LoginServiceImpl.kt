package org.example.kotlinjwtauth.security.service.auth

import jakarta.servlet.http.HttpServletRequest
import org.example.kotlinjwtauth.global.exception.BadRequestException
import org.example.kotlinjwtauth.security.payload.request.LoginRequest
import org.example.kotlinjwtauth.security.service.utils.HashService
import org.example.kotlinjwtauth.security.token.dto.TokensDto
import org.example.kotlinjwtauth.security.token.refresh.exception.InvalidRefreshTokenException
import org.example.kotlinjwtauth.security.token.refresh.model.RefreshToken
import org.example.kotlinjwtauth.security.token.refresh.service.RefreshTokenService
import org.example.kotlinjwtauth.security.token.service.TokenUtils
import org.example.kotlinjwtauth.security.user.model.UserDetailsImpl
import org.example.kotlinjwtauth.user.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.system.measureTimeMillis

@Service
@Transactional(readOnly = true)
class LoginServiceImpl(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val tokenUtils: TokenUtils,
    private val refreshTokenService: RefreshTokenService,
    private val hashService: HashService
) : LoginService {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(noRollbackFor = [BadCredentialsException::class])
    override fun login(loginRequest: LoginRequest): TokensDto {
        val accessTokenCookie: ResponseCookie
        val refreshTokenCookie: ResponseCookie
        val authentication: Authentication
        println(measureTimeMillis {
            authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
        })
        val userDetails = Objects.requireNonNull(authentication).principal as UserDetailsImpl

        accessTokenCookie = tokenUtils.generateJwtCookieByUserId(userDetails.id!!)
        refreshTokenCookie = refreshTokenService.createRefreshTokenByUserId(userDetails.user)

        log.debug("User logged in: id={}, username={}", userDetails.id, userDetails.username)


        return TokensDto(
            accessTokenCookie,
            refreshTokenCookie
        )
    }

    @Transactional
    override fun logout(httpServletRequest: HttpServletRequest): TokensDto {
        val refreshToken = tokenUtils.getRefreshTokenFromCookies(httpServletRequest)
            ?: throw BadRequestException()
        val idFromRefreshToken: String
        try {
            idFromRefreshToken = tokenUtils.getIdFromRefreshToken(refreshToken)
        } catch (e: IllegalArgumentException) {
            throw BadRequestException()
        }
        val id = idFromRefreshToken.toLong()
        if (refreshToken.isNotBlank() && refreshTokenService.existsById(id)) {
            refreshTokenService.deleteTokenById(id)
        }

        //todo async
        val jwtAccessCookie: ResponseCookie = tokenUtils.getCleanAccessTokenCookie()
        val jwtRefreshCookie: ResponseCookie = tokenUtils.getCleanRefreshTokenCookie()

        log.debug("User logged out")
        return TokensDto(
            jwtAccessCookie,
            jwtRefreshCookie
        )
    }

    @Transactional
    override fun refreshToken(request: HttpServletRequest): TokensDto {
        val refreshToken = tokenUtils.getRefreshTokenFromCookies(request)

        if (refreshToken.isNullOrBlank()) {
            log.warn("Invalid refresh token")
            throw InvalidRefreshTokenException()
        }
        val valid = tokenUtils.validateJwtRefreshToken(refreshToken)
        if (!valid) {
            throw InvalidRefreshTokenException()
        }
        val idFromRefreshToken = tokenUtils.getIdFromRefreshToken(refreshToken)

        val token: RefreshToken =
            refreshTokenService.findById(idFromRefreshToken)
                .orElseThrow { InvalidRefreshTokenException() }

        val matches = hashService.verifySHA256Hash(refreshToken, token.hashedToken!!)
        if (!matches) {
            log.warn("Refresh token hash doesnt match")
            throw InvalidRefreshTokenException()
        }
        val newRefreshTokenCookie = tokenUtils.generateRefreshTokenCookie(token.id!!)

        refreshTokenService.updateTokenValueAndEncode(newRefreshTokenCookie.value, token.id!!)

        val accessTokenCookie = tokenUtils.generateJwtCookieByUserId(token.user.id!!)

        //
        log.debug("Refresh token: userId={}, tokenId={}", token.user.id, token.id)
        return TokensDto(
            accessTokenCookie,
            newRefreshTokenCookie
        )
    }
}

