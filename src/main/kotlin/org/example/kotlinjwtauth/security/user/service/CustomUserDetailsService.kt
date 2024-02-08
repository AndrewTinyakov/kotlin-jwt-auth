package org.example.kotlinjwtauth.security.user.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

interface CustomUserDetailsService : UserDetailsService {
    fun loadUserById(id: String?): UserDetails?
}
