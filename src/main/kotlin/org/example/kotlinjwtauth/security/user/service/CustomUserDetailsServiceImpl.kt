package org.example.kotlinjwtauth.security.user.service

import org.example.kotlinjwtauth.security.constraint.exception.AuthExceptionMessageConstants.BAD_CREDENTIALS
import org.example.kotlinjwtauth.security.user.model.UserDetailsImpl
import org.example.kotlinjwtauth.user.service.UserService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CustomUserDetailsServiceImpl(private val userService: UserService) : CustomUserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findUserByUsername(username)
        user ?: throw BadCredentialsException(BAD_CREDENTIALS)

        return UserDetailsImpl.build(user)
    }

    override fun loadUserById(id: String): UserDetails {
        val user = userService.findOptionalUserById(id.toLong())
        user ?: throw BadCredentialsException(BAD_CREDENTIALS)

        return UserDetailsImpl.build(user)
    }
}
