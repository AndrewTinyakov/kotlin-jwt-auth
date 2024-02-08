package org.example.kotlinjwtauth.security.payload.request

import org.example.kotlinjwtauth.user.validation.annotation.EmailValidation
import org.example.kotlinjwtauth.user.validation.annotation.PasswordValidation
import org.example.kotlinjwtauth.user.validation.annotation.UsernameValidation

class SignUpRequest {
    @UsernameValidation
    private val username: String? = null

    @EmailValidation
    private val email: String? = null

    @PasswordValidation
    private val password: String? = null
}
