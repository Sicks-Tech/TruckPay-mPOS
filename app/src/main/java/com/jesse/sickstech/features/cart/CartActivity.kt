package com.jesse.sickstech.features.cart

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jesse.sickstech.R
import com.jesse.sickstech.core.util.setupToolbar
import com.jesse.sickstech.data.repository.cart.CartRepository
import com.jesse.sickstech.databinding.ActivityCartBinding
import com.jesse.sickstech.features.payment.PaymentActivity

class CartActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityCartBinding.inflate(layoutInflater)
    }
    private val cartAdapter = CartAdapter()
    private val cartRepository = CartRepository()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding){
            includeToolbar.setupToolbar(
                title = "Carrinho",
                showKeyboard = true,
                onBack = {finish()}
            )

            buttonFinalizar.setOnClickListener {
                val intent = Intent(this@CartActivity, PaymentActivity::class.java)
                startActivity(intent)
            }

            cartAdapter.atualizarListaDados(cartRepository.getCardItens())

            cartRecyclerView.adapter = cartAdapter
            cartRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        }

    }
}