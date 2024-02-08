package org.example.kotlinjwtauth.security.payload.request

import org.example.kotlinjwtauth.user.validation.annotation.EmailValidation
import org.example.kotlinjwtauth.user.validation.annotation.PasswordValidation
import org.example.kotlinjwtauth.user.validation.annotation.UsernameValidation

data class SignUpRequest(
    @field:UsernameValidation
    val username: String,
    @field:EmailValidation
    val email: String,
    @field:PasswordValidation
    val password: String
)

