package com.jesse.sickstech.features.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jesse.sickstech.R
import com.jesse.sickstech.core.util.CurrencyFormatter
import com.jesse.sickstech.core.util.setupToolbar
import com.jesse.sickstech.data.repository.OrderRepository
import com.jesse.sickstech.data.repository.ShopRepository
import com.jesse.sickstech.databinding.ActivityCartBinding
import com.jesse.sickstech.features.payment.PaymentActivity
import kotlinx.coroutines.launch
import okhttp3.internal.format

class CartActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityCartBinding.inflate(layoutInflater)
    }
    private val cartAdapter = CartAdapter()

    private val viewModel: CartViewModel by lazy {
        val repository = OrderRepository.getInstance(this)
        CartViewModel(repository)
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


        with(binding){

            includeToolbar.setupToolbar(
                title = "Carrinho",
                showKeyboard = true,
                onBack = {finish()}
            )

            buttonExcluir.setOnClickListener {
                val dialog = AlertDialog.Builder(this@CartActivity)
                    .setTitle("Limpar Carrinho")
                    .setMessage("Tem certeza que deseja limpar o carrinho?")
                    .setPositiveButton("Sim") { _, _ ->
                        viewModel.clearCart()
                        Toast.makeText(this@CartActivity, "Carrinho Limpo com sucesso", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Não", null)

                dialog.show()
            }

            buttonFinalizar.setOnClickListener {
                val intent = Intent(this@CartActivity, PaymentActivity::class.java)
                startActivity(intent)
            }

            cartRecyclerView.adapter = cartAdapter
            cartRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.cartItems.collect { itens ->
                        Log.d("CartActivity", "Itens no carrinho: $itens")
                        cartAdapter.atualizarListaDados(itens)

                        // Dica: Aproveite para atualizar o valor total da tela aqui também
//                        atualizarTotalCarrinho(itens)
                    }
                }
            }

            lifecycleScope.launch {
                viewModel.totalCart.collect { total ->
                    binding.textViewValor.text = CurrencyFormatter.format(total)
                }
            }
        }

    }
}