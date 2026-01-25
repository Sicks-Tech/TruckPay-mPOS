package com.jesse.sickstech.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {

    private val _state = MutableLiveData<LoginState>(LoginState.Idle)
    val state: LiveData<LoginState> = _state



    fun login(pin: String) {
        when{
            pin.isBlank() ->
                _state.value = LoginState.Error("Informe o PIN")
            pin.length != 6 ->
                _state.value = LoginState.Error("O PIN deve conter 6 dígitos")
            pin == "123456" ->
                _state.value = LoginState.Success
            else ->
                _state.value = LoginState.Error("PIN incorreto")
        }
    }
}