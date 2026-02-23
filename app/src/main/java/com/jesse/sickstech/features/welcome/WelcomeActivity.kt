package com.jesse.sickstech.features.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jesse.sickstech.R
import com.jesse.sickstech.core.navigation.BottomBarActivity
import com.jesse.sickstech.core.session.SessionManager
import com.jesse.sickstech.core.session.SessionStorage
import com.jesse.sickstech.databinding.ActivityWelcomeBinding
import com.jesse.sickstech.features.login.LoginActivity


class WelcomeActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }

    private val sessionManager by lazy {
        SessionManager(
            SessionStorage(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        with(binding) {
            button.setOnClickListener {
                decideNavigation()
            }
        }

    }

    private fun decideNavigation() {
        val nextActivity = if (sessionManager.isSessionValid()) {
            BottomBarActivity::class.java
        } else {
            LoginActivity::class.java
        }

        startActivity(
            Intent(this, nextActivity).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
    }
}