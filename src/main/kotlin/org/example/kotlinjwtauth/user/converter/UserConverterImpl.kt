package org.example.kotlinjwtauth.user.converter

import org.example.kotlinjwtauth.security.user.model.UserRole
import org.example.kotlinjwtauth.security.user.payload.response.CurrentUserResponse
import org.example.kotlinjwtauth.user.model.User
import org.example.kotlinjwtauth.user.payload.response.UserDataResponse
import org.springframework.stereotype.Component

@Component
class UserConverterImpl : UserConverter {
    override fun covertUserToResponse(user: User): UserDataResponse {
        return UserDataResponse(
            user.id!!,
            user.username,
            user.email
        )
    }

    override fun convertCurrentUserToResponse(user: User): CurrentUserResponse {
        return CurrentUserResponse(
            user.id!!,
            user.username,
            user.roles.map { role ->
                UserRole(
                    id = role.id!!,
                    name = role.name
                )
            }.toSet()
        )
    }

}
