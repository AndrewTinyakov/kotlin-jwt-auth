package org.example.kotlinjwtauth.user.service

import org.example.kotlinjwtauth.security.constraint.exception.AuthExceptionMessageConstants.BAD_CREDENTIALS
import org.example.kotlinjwtauth.security.exception.ForbiddenException
import org.example.kotlinjwtauth.security.user.model.UserDetailsImpl
import org.example.kotlinjwtauth.user.model.User
import org.example.kotlinjwtauth.user.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val userRepository: UserRepository,
    @param:Lazy private val passwordEncoder: PasswordEncoder
) : UserService {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun findOptionalUserById(id: Long): User? {
        val user: User? = userRepository.findById(id).getOrNull()

        user?.let { u ->
            log.debug(
                "Found user by id: id=${u.id}, username=${u.username}",
            )
        }

        return user
    }

    @Transactional
    override fun register(user: User): User {
        user.password = passwordEncoder.encode(user.password)
        val savedUser = userRepository.save(user)

        log.debug("Registered user: id={}, username={}", savedUser.id, savedUser.username)
        return savedUser
    }

    override fun getCurrentUser(): User {
        val auth: Authentication = SecurityContextHolder.getContext().authentication

        if (auth is AnonymousAuthenticationToken) {
            throw BadCredentialsException(BAD_CREDENTIALS)
        }
        val principal: UserDetailsImpl = auth.principal as UserDetailsImpl

        val currentUser: User? = findOptionalUserById(principal.id!!)
        currentUser ?: throw ForbiddenException()

        log.debug("Got current user: id={}, username={}", currentUser.id, currentUser.username)
        return currentUser
    }

    override fun findUserByUsername(username: String): User? {
        val user = userRepository.findByUsername(username)

        user?.let { value: User ->
            log.debug(
                "Found user by username: id={}, username={}",
                value.id,
                value.username
            )
        }
        return user
    }

    override fun existsByUsername(username: String): Boolean {
        val exists = userRepository.existsByUsernameIgnoreCase(username.lowercase(Locale.getDefault()))

        log.debug("Found if user exists by username: username={}", username)

        return exists
    }

    override fun existsByEmail(email: String): Boolean {
        val exists = userRepository.existsByEmail(email)

        log.debug("Found if user exists by email: email={}", email)

        return exists
    }
}
