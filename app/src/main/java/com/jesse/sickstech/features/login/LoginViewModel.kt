package com.jesse.sickstech.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jesse.sickstech.core.security.PinValidator
import com.jesse.sickstech.core.session.SessionManager
import com.jesse.sickstech.data.repository.pin.PinRepository
import kotlin.jvm.java



class LoginViewModel(
    private val pinRepository: PinRepository,
    private val sessionsManager: SessionManager
) : ViewModel() {

    private val _state = MutableLiveData<LoginState>(LoginState.Idle)
    val state: LiveData<LoginState> = _state



    fun login(pin: String) {
        if (pin.isBlank() || pin.length != 6) {
            _state.value = LoginState.Error("PIN inválido")
            return
        }

        _state.value = LoginState.Loading

        if (!pinRepository.hasPin()) {
            pinRepository.createPin(pin)
            sessionsManager.startSession()
            _state.value = LoginState.Success
            return
        }

        if (pinRepository.validate(pin)) {
            sessionsManager.startSession()
            _state.value = LoginState.Success
        } else {
            _state.value = LoginState.Error("PIN incorreto")
        }
    }
}