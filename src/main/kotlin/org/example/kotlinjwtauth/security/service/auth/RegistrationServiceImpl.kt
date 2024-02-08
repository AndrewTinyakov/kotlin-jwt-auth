package org.example.kotlinjwtauth.security.service.auth

import org.example.kotlinjwtauth.global.config.utils.AppInit
import org.example.kotlinjwtauth.security.exception.registration.TakenEmailException
import org.example.kotlinjwtauth.security.exception.registration.TakenUsernameAndEmailException
import org.example.kotlinjwtauth.security.exception.registration.TakenUsernameException
import org.example.kotlinjwtauth.security.payload.request.SignUpRequest
import org.example.kotlinjwtauth.security.user.model.Role
import org.example.kotlinjwtauth.security.user.model.UserRole
import org.example.kotlinjwtauth.security.user.service.RoleService
import org.example.kotlinjwtauth.user.model.User
import org.example.kotlinjwtauth.user.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RegistrationServiceImpl(
    private val userService: UserService,
    private val roleService: RoleService,
    private val appInit: AppInit
) : RegistrationService {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun registration(signUpRequest: SignUpRequest) {
        validateRegistration(signUpRequest)
        val user = createUser(signUpRequest)
        log.debug("User registered: id={}, username={}", user.id, user.username)
    }

    private fun validateRegistration(request: SignUpRequest) {
        val existsByEmail = userService.existsByEmail(request.email)
        val existsByUsername = userService.existsByUsername(request.username)

        if (existsByEmail && existsByUsername) {
            log.warn("User was not registered: taken username and email")
            throw TakenUsernameAndEmailException()
        }
        if (existsByUsername) {
            log.warn("User was not registered: taken username")
            throw TakenUsernameException()
        }
        if (existsByEmail) {
            log.warn("User was not registered: taken email")
            throw TakenEmailException()
        }
    }

    private fun createUser(signUpRequest: SignUpRequest): User {
        val roles: MutableSet<UserRole> = HashSet()
        var userRole: UserRole? = roleService.findRoleByName(Role.ROLE_USER)
            .orElse(null)
        if (userRole == null) {
            log.warn("Roles are null")
            appInit.insertRoles()
            userRole = roleService.findRoleByName(Role.ROLE_USER)
                .orElseThrow() as UserRole
        }
        roles.add(userRole)
        val user = User(signUpRequest, roles)

        return userService.register(user)
    }
}
