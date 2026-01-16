package com.jesse.sickstech.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {

    private val _loginSuccess = MutableLiveData<Unit>()
    val loginSuccess: LiveData<Unit> = _loginSuccess


    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> = _loginError

    fun login(pin: String) {
        when {
            pin.isBlank() ->
                _loginError.value = "Informe o PIN"

            pin.length != 6 ->
                _loginError.value = "PIN deve ter 6 dígitos"

            pin == "123456" ->
                _loginSuccess.value = Unit

            else ->
                _loginError.value = "PIN incorreto"
        }
    }
}