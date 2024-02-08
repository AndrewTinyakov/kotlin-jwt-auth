package org.example.kotlinjwtauth.security.user.service

import org.example.kotlinjwtauth.security.user.model.Role
import org.example.kotlinjwtauth.security.user.model.UserRole
import java.util.*


interface RoleService {
    fun saveRole(role: UserRole)

    fun findRoleByName(roleName: Role): Optional<UserRole>
}
