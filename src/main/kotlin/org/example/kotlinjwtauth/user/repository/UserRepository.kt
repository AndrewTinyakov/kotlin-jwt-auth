package org.example.kotlinjwtauth.user.repository

import org.example.kotlinjwtauth.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User?, Long?> {
    fun findByUsername(username: String?): Optional<User?>?

    fun existsByUsernameIgnoreCase(username: String?): Boolean

    fun existsByEmail(email: String?): Boolean
}
