package org.example.kotlinjwtauth.user.controller

import org.example.kotlinjwtauth.global.exception.NotFoundException
import org.example.kotlinjwtauth.security.user.payload.response.CurrentUserResponse
import org.example.kotlinjwtauth.user.converter.UserConverter
import org.example.kotlinjwtauth.user.payload.response.UserDataResponse
import org.example.kotlinjwtauth.user.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService, private val userConverter: UserConverter) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{username}")
    fun findUserByUsername(@PathVariable username: String): UserDataResponse {
        log.debug("Get user request: username={}", username)

        val user = userService.findUserByUsername(username)
        user ?: throw NotFoundException()

        return userConverter.covertUserToResponse(user)
    }

    @GetMapping("/current-user")
    fun currentUser(): CurrentUserResponse {
        log.debug("Get current user request")

        val currentUser = userService.getCurrentUser()
        return userConverter.convertCurrentUserToResponse(currentUser)
    }

}