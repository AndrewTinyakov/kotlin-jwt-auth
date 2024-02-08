package org.example.kotlinjwtauth.security.payload.request

import lombok.AllArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginRequest {
    @UsernameValidation
    private val username: String? = null

    @PasswordValidation
    private val password: String? = null
}
