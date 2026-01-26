package com.jesse.sickstech.core.security

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PinHasher {

    fun hash(pin: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(
            pin.toCharArray(),
            salt,
            10_000,
            256
        )

        return SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(spec)
            .encoded
    }
}
