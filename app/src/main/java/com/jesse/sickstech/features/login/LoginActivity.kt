package com.jesse.sickstech.features.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
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
    private val viewModel : LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       viewModel.state.observe(this){ state ->
           when (state) {
               is LoginState.Idle -> {}
               is LoginState.Loading -> {
                   Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
               }
               is LoginState.Success -> {
                   val intent = Intent(this, BottomBarActivity::class.java)
                   intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                   startActivity(intent)
               }
               is LoginState.Error -> {
                   Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
               }
               else -> {
                   Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
               }
           }
       }

        with(binding){
            buttonLogin.setOnClickListener {
                viewModel.login(editTextPin.text.toString())
            }
        }
    }


}