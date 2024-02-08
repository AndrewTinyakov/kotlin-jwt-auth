package org.example.kotlinjwtauth.user.service

import org.example.kotlinjwtauth.user.model.User

interface UserService {
    fun findOptionalUserById(id: Long): User?

    fun register(user: User): User

    fun getCurrentUser(): User

    fun findUserByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}
