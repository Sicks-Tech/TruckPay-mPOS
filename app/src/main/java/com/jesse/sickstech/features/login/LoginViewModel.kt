package com.jesse.sickstech.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jesse.sickstech.core.security.PinValidator
import com.jesse.sickstech.core.session.SessionManager
import com.jesse.sickstech.data.repository.pin.PinRepository
import com.jesse.sickstech.domain.model.LoginState
import kotlin.jvm.java



class LoginViewModel(
    private val pinRepository: PinRepository,
    private val sessionsManager: SessionManager
) : ViewModel() {

}