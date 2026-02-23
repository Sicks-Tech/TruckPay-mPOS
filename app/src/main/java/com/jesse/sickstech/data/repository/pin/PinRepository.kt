package com.jesse.sickstech.data.repository.pin

import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import com.jesse.sickstech.core.security.PinHasher
import java.security.SecureRandom

class PinRepository(
    private val storage: EncryptedSharedPreferences,
    private val hasher: PinHasher,
) {

    fun hasPin(): Boolean {
        return storage.contains("pin_hash")
    }

    fun createPin(pin: String) {
        val salt = SecureRandom().generateSeed(16)
        val hash = hasher.hash(pin, salt)

        storage.edit {
            putString("pin_hash", Base64.encodeToString(hash, Base64.NO_WRAP))
            putString("pin_salt", Base64.encodeToString(salt, Base64.NO_WRAP))
        }
    }

    fun validate(pin: String): Boolean {
        val savedHash = Base64.decode(storage.getString("pin_hash", null), Base64.NO_WRAP)
        val salt = Base64.decode(storage.getString("pin_salt", null), Base64.NO_WRAP)

        val inputHash = hasher.hash(pin, salt)
        return inputHash.contentEquals(savedHash)
    }
}
