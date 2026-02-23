package com.jesse.sickstech.features.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.jesse.sickstech.R
import com.jesse.sickstech.core.util.setupToolbar
import com.jesse.sickstech.databinding.ActivityPaymentBinding
import com.jesse.sickstech.features.paymentProcess.PaymentProcessActivity

class PaymentActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
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

            val cards = listOf(cardDebito, cardCredito,cardVoucher, cardPix)

            cards.forEach { card ->
                card.setOnClickListener {
                    cards.forEach { it.isChecked = false }
                    card.isChecked = true
                    Log.d("TAG", "onCreate: ${card.tag}")
                    openProcessing()
                }
            }


        }

    }
    fun openProcessing(){
        val intent = Intent(this, PaymentProcessActivity::class.java)
        startActivity(intent)
    }
}