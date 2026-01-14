package com.jesse.sickstech.features.cart

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.jesse.sickstech.R
import com.jesse.sickstech.core.util.setupToolbar
import com.jesse.sickstech.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityCartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.includeToolbar.setupToolbar(
            title = "Carrinho",
            showKeyboard = true,
            onBack = {binding.root.findNavController().popBackStack()}
        )
    }
}