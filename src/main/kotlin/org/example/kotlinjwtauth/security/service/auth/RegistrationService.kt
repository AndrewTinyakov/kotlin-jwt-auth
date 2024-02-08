package org.example.kotlinjwtauth.security.service.auth

import org.example.kotlinjwtauth.security.payload.request.SignUpRequest


interface RegistrationService {
    fun registration(signUpRequest: SignUpRequest)
}
