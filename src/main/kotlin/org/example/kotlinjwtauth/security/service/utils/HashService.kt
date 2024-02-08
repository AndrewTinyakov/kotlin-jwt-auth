package org.example.kotlinjwtauth.security.service.utils

interface HashService {
    fun hashStringWithSHA256(input: String): String

    fun verifySHA256Hash(value: String, hashedValue: String): Boolean
}
