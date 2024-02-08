package org.example.kotlinjwtauth.user.payload.response


@JvmRecord
data class UserDataResponse(
    val id: Long,
    val username: String,
    val email: String
)
