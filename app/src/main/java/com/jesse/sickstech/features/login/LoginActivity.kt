package com.jesse.sickstech.features.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jesse.sickstech.R
import com.jesse.sickstech.core.navigation.BottomBarActivity
import com.jesse.sickstech.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.loginSuccess.observe(this) {
            startActivity(Intent(this, BottomBarActivity::class.java))
            finish()
        }

        viewModel.loginError.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        with(binding){
            buttonLogin.setOnClickListener {
                viewModel.login(editTextPin.text.toString())
            }
        }
    }


}