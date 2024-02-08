package org.example.kotlinjwtauth.global.constant.validation


object ValidationMessageConstants {
    private const val SIZE_MESSAGE = " must be at least {min} characters and no more than {max} characters"
    private const val REQUIRED_MESSAGE = " is required"
    private const val NOT_PASSED_REGEXP = " haven't passed regexp"

    private const val EMAIL_FIELD_NAME = "email"
    const val EMAIL_MESSAGE: String = "Must be an $EMAIL_FIELD_NAME"
    const val EMAIL_SIZE_MESSAGE: String = EMAIL_FIELD_NAME + SIZE_MESSAGE
    const val EMAIL_REQUIRED_MESSAGE: String = EMAIL_FIELD_NAME + REQUIRED_MESSAGE

    private const val PASSWORD_FIELD_NAME = "Password"
    const val PASSWORD_SIZE_MESSAGE: String = PASSWORD_FIELD_NAME + SIZE_MESSAGE
    const val PASSWORD_REQUIRED_MESSAGE: String = PASSWORD_FIELD_NAME + REQUIRED_MESSAGE

    private const val USERNAME_FIELD_NAME = "Username"
    const val USERNAME_NOT_PASSED_REGEXP_MESSAGE: String = USERNAME_FIELD_NAME + NOT_PASSED_REGEXP
    const val USERNAME_SIZE_MESSAGE: String = USERNAME_FIELD_NAME + SIZE_MESSAGE
    const val USERNAME_REQUIRED_MESSAGE: String = USERNAME_FIELD_NAME + REQUIRED_MESSAGE
}
