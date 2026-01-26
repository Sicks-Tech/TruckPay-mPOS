package com.jesse.sickstech.features.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.jesse.sickstech.core.security.PinHasher
import com.jesse.sickstech.core.session.SessionManager
import com.jesse.sickstech.core.session.SessionStorage
import com.jesse.sickstech.data.repository.pin.PinRepository

// basicamente a forma que o hilt já faz , só que por debaixo do panos
//o android não sabe montar view model com paremetros , por isso essa forma diferente
class LoginViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {

    private val securePrefs by lazy {
        EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val pinRepository by lazy {
        PinRepository(
            securePrefs as EncryptedSharedPreferences,
            PinHasher()
        )
    }

    private val sessionManager by lazy {
        SessionManager(SessionStorage(context))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == LoginViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                pinRepository = pinRepository,
                sessionsManager = sessionManager
            ) as T
        }
        throw IllegalArgumentException()
    }
}

