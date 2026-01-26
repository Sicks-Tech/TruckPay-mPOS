package com.jesse.sickstech.core.session

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

class SessionStorage(context: Context) {

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "session_prefs",
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun save(state: SessionState) {
        prefs.edit {
            putString("session_id", state.sessionId)
                .putLong("last_login_at", state.lastLoginAt)
                .putLong("expires_at", state.expiresAt)
        }
    }

    fun load(): SessionState? {
        val id = prefs.getString("session_id", null) ?: return null
        return SessionState(
            sessionId = id,
            lastLoginAt = prefs.getLong("last_login_at", 0),
            expiresAt = prefs.getLong("expires_at", 0)
        )
    }

    fun clear() {
        prefs.edit { clear() }
    }
}
