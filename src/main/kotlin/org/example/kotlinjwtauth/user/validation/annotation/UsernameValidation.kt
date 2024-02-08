package org.example.kotlinjwtauth.user.validation.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class UsernameValidation(
    val message: String = "Invalid username",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload?>> = []
)
