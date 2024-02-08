package org.example.kotlinjwtauth.security.user.service

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
        val currentUserView = userService.findUserByUsername(username)
            .orElseThrow<BadCredentialsException> { BadCredentialsException(BAD_CREDENTIALS) }

        return UserDetailsImpl.build(currentUserView)
    }

    override fun loadUserById(id: String): UserDetails {
        val currentUserView = userService.findOptionalUserById(id.toLong())
            .orElseThrow<BadCredentialsException> { BadCredentialsException(BAD_CREDENTIALS) }

        return UserDetailsImpl.build(currentUserView)
    }
}
