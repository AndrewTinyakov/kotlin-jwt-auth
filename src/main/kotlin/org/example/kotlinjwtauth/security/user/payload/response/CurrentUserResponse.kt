package org.example.kotlinjwtauth.security.user.payload.response

import org.example.kotlinjwtauth.security.user.model.UserRole

@JvmRecord
data class CurrentUserResponse(
    val id: Long,
    val username: String,
    val roles: Set<UserRole>
)
