package org.example.kotlinjwtauth.security.user.service

import org.example.kotlinjwtauth.security.user.model.Role
import org.example.kotlinjwtauth.security.user.model.UserRole
import org.example.kotlinjwtauth.security.user.repository.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class RoleServiceImpl(private val roleRepository: RoleRepository) : RoleService {

    @Transactional
    override fun saveRole(role: UserRole) {
        roleRepository.save(role)
    }

    override fun findRoleByName(
        roleName: Role
    ): Optional<UserRole> {
        return roleRepository.findByName(roleName)
    }

}
