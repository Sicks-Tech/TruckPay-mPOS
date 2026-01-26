package com.jesse.sickstech.core.session

import java.util.UUID
import java.util.concurrent.TimeUnit

class SessionManager(
    private val storage: SessionStorage
) {

    private val SESSION_DURATION =
        TimeUnit.HOURS.toMillis(8)

    fun startSession(): SessionState {
        val now = System.currentTimeMillis()

        val state = SessionState(
            sessionId = UUID.randomUUID().toString(),
            lastLoginAt = now,
            expiresAt = now + SESSION_DURATION
        )

        storage.save(state)
        return state
    }

    fun isSessionValid(): Boolean {
        val session = storage.load() ?: return false
        return System.currentTimeMillis() < session.expiresAt
    }

    fun invalidate() {
        storage.clear()
    }
}
