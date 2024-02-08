package org.example.kotlinjwtauth.security.service.utils

import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Service
class HashServiceImpl : HashService {
    override fun hashStringWithSHA256(input: String): String {
        try {
            val md = MessageDigest.getInstance("SHA-256")
            val hashBytes = md.digest(input.toByteArray())

            val hexString = StringBuilder()
            for (b in hashBytes) {
                hexString.append(String.format("%02x", b))
            }

            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("SHA-256 algorithm not available", e)
        }
    }

    override fun verifySHA256Hash(value: String, hashedValue: String): Boolean {
        val hashedInput = hashStringWithSHA256(value)
        return hashedInput == hashedValue
    }
}
