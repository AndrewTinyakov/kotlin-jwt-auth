package org.example.kotlinjwtauth.user.validation.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.example.kotlinjwtauth.global.constant.validation.ValidationMessageConstants
import org.example.kotlinjwtauth.global.constant.validation.ValidationSizeConstants
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
@NotNull(message = ValidationMessageConstants.PASSWORD_REQUIRED_MESSAGE)
@NotBlank(message = ValidationMessageConstants.PASSWORD_REQUIRED_MESSAGE)
@Size(
    min = ValidationSizeConstants.PASSWORD_MIN_SIZE,
    max = ValidationSizeConstants.PASSWORD_MAX_SIZE,
    message = ValidationMessageConstants.PASSWORD_SIZE_MESSAGE
)
annotation class PasswordValidation(
    val message: String = "Invalid password",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)







