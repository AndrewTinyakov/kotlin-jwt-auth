package org.example.kotlinjwtauth.security.payload.request

import org.example.kotlinjwtauth.user.validation.annotation.PasswordValidation
import org.example.kotlinjwtauth.user.validation.annotation.UsernameValidation

data class LoginRequest(
    @field:UsernameValidation
    val username: String,
    @field:PasswordValidation
    val password: String
)
