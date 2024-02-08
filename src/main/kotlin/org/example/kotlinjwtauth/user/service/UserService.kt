package org.example.kotlinjwtauth.user.service

import org.example.kotlinjwtauth.user.model.User
import java.util.*

interface UserService {
    fun findOptionalUserById(id: Long?): Optional<User?>?

    fun register(user: User?): User?

    val currentUser: User?

    fun findUserByUsername(username: String?): Optional<User?>?

    fun existsByUsername(username: String?): Boolean

    fun existsByEmail(email: String?): Boolean
}
