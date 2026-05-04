package com.jesse.sickstech.features.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.jesse.sickstech.R
import com.jesse.sickstech.core.navigation.BottomBarActivity
import com.jesse.sickstech.core.security.PinHasher
import com.jesse.sickstech.core.security.PinValidator
import com.jesse.sickstech.core.session.SessionManager
import com.jesse.sickstech.core.session.SessionStorage
import com.jesse.sickstech.databinding.ActivityLoginBinding
import com.jesse.sickstech.domain.model.LoginState

class LoginActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val auth by lazy{
        FirebaseAuth.getInstance()
    }

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding){
            buttonLogin.setOnClickListener {
                if (validarCampos()) {
                    logarUsuario()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }


    fun logarUsuario() {
        auth.signInWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            Toast.makeText(this, "Logado com sucesso!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, BottomBarActivity::class.java))
        }.addOnFailureListener { error ->
            try {
                throw error
            } catch (erroUserInvalido: FirebaseAuthInvalidUserException) {
                Toast.makeText(this, "Email invalido", Toast.LENGTH_SHORT).show()
            }catch (erroCredencialInvalida: FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(this, "Senha invalida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verificarUsuarioLogado() {
        val userAtual = auth.currentUser
        if(userAtual != null){
            startActivity(Intent(this, BottomBarActivity::class.java))
        }
    }

    fun validarCampos(): Boolean {
        email = binding.editTextEmail.text.toString()
        password = binding.editTextPassword.text.toString()

        if (email.isNotEmpty()) {
            binding.tILEmailLogin.error = null

            if (password.isNotEmpty()) {
                binding.tILPasswordLogin.error = null
                return true
            } else {
                binding.tILPasswordLogin.error = "Informe a senha"
                return false
            }

        } else {
            binding.tILEmailLogin.error = "Informe o email"
            return false
        }

    }
}