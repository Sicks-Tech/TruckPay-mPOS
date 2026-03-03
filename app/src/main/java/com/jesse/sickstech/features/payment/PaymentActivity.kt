package com.jesse.sickstech.features.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jesse.sickstech.R
import com.jesse.sickstech.core.util.CurrencyFormatter
import com.jesse.sickstech.core.util.setupToolbar
import com.jesse.sickstech.data.model.pos.OrderConfig
import com.jesse.sickstech.data.repository.OrderRepository
import com.jesse.sickstech.databinding.ActivityPaymentBinding
import com.jesse.sickstech.features.paymentProcess.PaymentProcessActivity
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }

    private val viewModel: PaymentViewModel by lazy {
        val repository = OrderRepository.getInstance(this)
        PaymentViewModel(repository)
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
                title = "Pagamento",
                showKeyboard = false,
                onBack = {finish()}
            )

            buttonVoltar.setOnClickListener {
                finish()
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.cartTotal.collect { total ->
                      textViewTotalValor.text = CurrencyFormatter.format(total)
                    }
                }
            }

            val cards = listOf(cardDebito, cardCredito,cardVoucher, cardPix)

            cards.forEach { card ->
                card.setOnClickListener {
                    cards.forEach { it.isChecked = false }
                    Log.d("TAG", "onCreate: ${card.tag}")

                    cards.forEach { it.isChecked = false }




                    lifecycleScope.launch {
                        try {

                            val orderId = viewModel.pagar(
                                accountId = 1,
                                storeId = 1,
                                paymentType = OrderConfig.DEBIT_CARD
                            )

                            openProcessing(card.tag.toString(), orderId)

                        } catch (e: Exception) {
                            Log.e("Payment", "Erro ao pagar: ${e.message}")
                        }
                    }
                }
            }


        }

    }

    fun openProcessing(type : String , orderId : Int){
        val intent = Intent(this, PaymentProcessActivity::class.java)
        intent.putExtra("payment_method", type)
        startActivity(intent)
    }
}