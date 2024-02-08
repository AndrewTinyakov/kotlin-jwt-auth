package org.example.kotlinjwtauth.user.service

import org.example.kotlinjwtauth.security.exception.ForbiddenException
import org.example.kotlinjwtauth.security.user.model.UserDetailsImpl
import org.example.kotlinjwtauth.user.model.User
import org.example.kotlinjwtauth.user.repository.UserRepository
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val userRepository: UserRepository,
    @param:Lazy private val passwordEncoder: PasswordEncoder
) : UserService {
    override fun findOptionalUserById(id: Long): Optional<User> {
        val user = userRepository.findById(id)

        user.ifPresent { value: User ->
            log.debug(
                "Found user by id: id={}, username={}",
                value.getId(), value.getUsername()
            )
        }

        return user
    }

    @Transactional
    override fun register(user: User): User {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()))
        }
        val savedUser = userRepository.save(user)

        log.debug("Registered user: id={}, username={}", savedUser.getId(), savedUser.getUsername())
        return savedUser
    }

    override fun getCurrentUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth is AnonymousAuthenticationToken) {
            throw BadCredentialsException(BAD_CREDENTIALS)
        }
        val principal = auth.principal as UserDetailsImpl

        val currentUser = findOptionalUserById(principal.id)
            .orElseThrow { ForbiddenException() }

        log.debug("Got current user: id={}, username={}", currentUser.getId(), currentUser.getUsername())
        return currentUser
    }

    override fun findUserByUsername(username: String): Optional<User> {
        val user = userRepository.findByUsername(username)

        user.ifPresent { value: User ->
            log.debug(
                "Found user by username: id={}, username={}",
                value.getId(),
                value.getUsername()
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
