package com.jesse.sickstech.core.session

data class SessionState(
    val sessionId: String,
    val expiresAt: Long,
    val lastLoginAt: Long
)