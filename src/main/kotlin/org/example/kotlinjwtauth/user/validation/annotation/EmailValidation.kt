package org.example.kotlinjwtauth.user.validation.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.Email
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
@NotNull(message = ValidationMessageConstants.EMAIL_REQUIRED_MESSAGE)
@NotBlank(message = ValidationMessageConstants.EMAIL_REQUIRED_MESSAGE)
@Email(message = ValidationMessageConstants.EMAIL_MESSAGE)
@Size(
    min = ValidationSizeConstants.EMAIL_MIN_SIZE,
    max = ValidationSizeConstants.EMAIL_MAX_SIZE,
    message = ValidationMessageConstants.EMAIL_SIZE_MESSAGE
)
annotation class EmailValidation(
    val message: String = "Invalid email",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)