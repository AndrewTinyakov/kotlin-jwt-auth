package org.example.kotlinjwtauth.global.config.utils

import org.example.kotlinjwtauth.security.user.model.Role
import org.example.kotlinjwtauth.security.user.model.UserRole
import org.example.kotlinjwtauth.security.user.service.RoleService
import org.springframework.stereotype.Component

@Component
class AppInitImpl(private val roleService: RoleService) : AppInit {
    override fun insertRoles() {
        //todo async
        roleService.saveRole(UserRole(Role.ROLE_ADMIN))
        roleService.saveRole(UserRole(Role.ROLE_USER))
    }
}
