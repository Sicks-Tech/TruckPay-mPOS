package com.jesse.sickstech.domain.model

sealed class LoginState {
    object Idle: LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}