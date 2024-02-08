package org.example.kotlinjwtauth.security.token.service

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils
import java.security.Key
import java.util.*


@Component
class TokenUtilsImpl : TokenUtils {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${jwt.access-secret}")
    private val accessJwtSecret: String? = null

    @Value("\${jwt.refresh-secret}")
    private val refreshJwtSecret: String? = null

    @Value("\${jwt.expiration-ms}")
    private val accessExpirationMs = 0

    @Value("\${jwt.refresh-expiration-ms}")
    private val refreshExpirationMs: Long = 0

    @Value("\${jwt.access-token-name}")
    private val accessTokenName: String? = null

    @Value("\${jwt.refresh-token-name}")
    private val refreshTokenName: String? = null

    override fun generateJwtCookieByUserId(userId: Long): ResponseCookie {
        val jwt = generateAccessTokenFromId(userId)
        return generateCookie(accessTokenName, jwt, false)
    }

    override fun generateRefreshTokenCookie(tokenId: Long): ResponseCookie {
        val refreshToken = generateRefreshTokenFromId(tokenId)
        return generateCookie(refreshTokenName, refreshToken, true)
    }

    override fun getAccessTokenFromCookies(request: HttpServletRequest): String? {
        return getCookieValueByName(request, accessTokenName!!)
    }

    override fun getRefreshTokenFromCookies(request: HttpServletRequest): String? {
        return getCookieValueByName(request, refreshTokenName!!)
    }

    override fun getCleanAccessTokenCookie(): ResponseCookie {
        return ResponseCookie.from(accessTokenName!!, "")
            .path("/")
            .build()
    }

    override fun getCleanRefreshTokenCookie(): ResponseCookie {
        return ResponseCookie.from(refreshTokenName!!, "")
            .path("/")
            .build()
    }

    override fun getIdFromAccessToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(accessSignInKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    override fun getIdFromRefreshToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(refreshSignInKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    private fun generateAccessTokenFromId(id: Long): String {
        return Jwts.builder()
            .setSubject(id.toString())
            .setIssuedAt(Date())
            .setExpiration(
                Date(
                    Date().time + accessExpirationMs
                )
            )
            .signWith(accessSignInKey, SignatureAlgorithm.HS512)
            .compact()
    }

    private fun generateRefreshTokenFromId(id: Long): String {
        return Jwts.builder()
            .setSubject(id.toString())
            .setIssuedAt(Date())
            .setExpiration(
                Date(
                    Date().time + refreshExpirationMs
                )
            )
            .signWith(refreshSignInKey, SignatureAlgorithm.HS512)
            .compact()
    }

    override fun validateJwtAccessToken(authToken: String?): Boolean {
        if(authToken == null){
            return false
        }
        return validateJwt(authToken, accessSignInKey)
    }

    override fun validateJwtRefreshToken(refreshToken: String): Boolean {
        return validateJwt(refreshToken, refreshSignInKey)
    }

    private fun validateJwt(token: String, key: Key): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            log.warn("Invalid $token signature: ${e.message}")
        } catch (e: MalformedJwtException) {
            log.warn("Invalid $token token: ${e.message}")
        } catch (e: ExpiredJwtException) {
            log.debug("$token token is expired: ${e.message}")
        } catch (e: UnsupportedJwtException) {
            log.warn("$token token is unsupported: ${e.message}}")
        } catch (e: IllegalArgumentException) {
            log.debug("$token claims string is empty: ${e.message}}")
        }

        return false
    }

    private fun generateCookie(name: String?, value: String, httpOnly: Boolean): ResponseCookie {
        return ResponseCookie.from(name!!, value)
            .path("/")
            .maxAge((24 * 60 * 60).toLong())
            .httpOnly(httpOnly)
            .sameSite("Strict")
            .build()
    }

    private fun getCookieValueByName(request: HttpServletRequest, name: String): String? {
        val cookie = WebUtils.getCookie(request, name)
        return cookie?.value
    }

    private val accessSignInKey: Key
        get() {
            val keyBytes = Decoders.BASE64.decode(accessJwtSecret)
            return Keys.hmacShaKeyFor(keyBytes)
        }

    private val refreshSignInKey: Key
        get() {
            val keyBytes = Decoders.BASE64.decode(refreshJwtSecret)
            return Keys.hmacShaKeyFor(keyBytes)
        }
}
