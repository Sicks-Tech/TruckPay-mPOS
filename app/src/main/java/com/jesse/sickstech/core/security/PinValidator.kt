package com.jesse.sickstech.core.security

class PinValidator(
    private val hasher: PinHasher
) {

    // ⚠️ MVP — depois isso sai do código
    private val salt = byteArrayOf(
        12, 4, 9, 23, 88, 3, 17, 91
    )

    private val pinHash = byteArrayOf(
        0x3A, 0x7F, 0x12, 0x9C.toByte(), 0x01, 0x22, 0x6B, 0x5E
    )

    fun isValid(inputPin: String): Boolean {
        val inputHash = hasher.hash(inputPin, salt)
        return inputHash.contentEquals(pinHash)
    }
}
