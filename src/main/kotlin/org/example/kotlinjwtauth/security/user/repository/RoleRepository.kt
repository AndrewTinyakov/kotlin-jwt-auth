package org.example.kotlinjwtauth.security.user.repository

import org.example.kotlinjwtauth.security.user.model.Role
import org.example.kotlinjwtauth.security.user.model.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<UserRole, Long> {
    fun findByName(name: Role): Optional<UserRole>
}
