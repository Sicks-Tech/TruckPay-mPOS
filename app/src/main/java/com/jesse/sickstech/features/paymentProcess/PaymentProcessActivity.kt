package com.jesse.sickstech.features.paymentProcess

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.jesse.sickstech.R
import com.jesse.sickstech.core.util.setupToolbar
import com.jesse.sickstech.data.AppDataBase
import com.jesse.sickstech.data.api.RetrofitHelper
import com.jesse.sickstech.databinding.ActivityPaymentProcessBinding
import com.jesse.sickstech.domain.enums.OrderStatus
import com.jesse.sickstech.features.confirmation.ConfirmationActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.concurrent.timer

class PaymentProcessActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPaymentProcessBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: PaymentProcessViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val api = RetrofitHelper.api
        val database = AppDataBase.getInstance(this)
        val orderDao = database.orderDAO()

        val factory = PaymentProcessViewModelFactory(api, orderDao)

        viewModel = ViewModelProvider(this, factory)
            .get(PaymentProcessViewModel::class.java)

        val paymentType = intent.getStringExtra("payment_method")
        val orderId = intent.getStringExtra("MP_ORDER_ID")
            ?: run {
                finish()
                return
            }
        Log.d("STATUS", "Received orderId: $orderId")



        with(binding) {
            includeToolbar.setupToolbar(
                title = "Pagamento",
                showKeyboard = false,
                onBack = { finish() }
            )

            textViewFormaPagamento.text = paymentType

            buttonVoltar.setOnClickListener {
                finish()
            }
        }


        orderId.let { id ->

            lifecycleScope.launch {

                // 1️⃣ Observa mudanças do banco
                launch {
                    viewModel.observeOrderByMpId(id).collect { order ->

                        if (order == null) return@collect

                        Log.d("STATUS", order.status.toString())

                        when (order.status) {

                            OrderStatus.PROCESSED -> {
                                binding.textViewProcessando.text = "Pagamento Confirmado"

                                delay(1000)

                                startActivity(
                                    Intent(
                                        this@PaymentProcessActivity,
                                        ConfirmationActivity::class.java
                                    )
                                )
                                finish()
                            }

                            OrderStatus.PENDING -> {
                                binding.textViewProcessando.text = "Aguardando Pagamento..."
                            }

                            OrderStatus.CANCELED,
                            OrderStatus.FAILED -> {
                                binding.textViewProcessando.text = "Cancelado"
                                delay(1000)
                                finish()
                            }

                            else -> {}
                        }
                    }
                }

                // 2️⃣ Polling separado
                launch {
                    while (isActive) {
                        viewModel.checkOrderStatus(id)
                        delay(3000)
                    }
                }
            }
        }
    }
}