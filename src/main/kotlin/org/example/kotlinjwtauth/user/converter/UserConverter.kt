package org.example.kotlinjwtauth.user.converter

import org.example.kotlinjwtauth.security.user.payload.response.CurrentUserResponse
import org.example.kotlinjwtauth.user.model.User
import org.example.kotlinjwtauth.user.payload.response.UserDataResponse

interface UserConverter {
    fun covertUserToResponse(user: User?): UserDataResponse?

    fun convertCurrentUserToResponse(user: User?): CurrentUserResponse?
}
