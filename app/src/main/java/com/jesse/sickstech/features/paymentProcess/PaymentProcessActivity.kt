package com.jesse.sickstech.features.paymentProcess

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scale
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.jesse.sickstech.R
import com.jesse.sickstech.core.util.BluetoothDeviceHelper
import com.jesse.sickstech.core.util.setupToolbar
import com.jesse.sickstech.data.AppDataBase
import com.jesse.sickstech.data.api.RetrofitHelper
import com.jesse.sickstech.data.printer.BluetoothPrinterManager
import com.jesse.sickstech.data.printer.EscPosProcessor
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

    private var reciboJaImpresso = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                Log.d("PRINT_DEBUG", "Permissões de Bluetooth concedidas pelo usuário!")
            } else {
                Log.e("PRINT_DEBUG", "Usuário negou as permissões de Bluetooth!")
            }
        }

        // 🟢 ADICIONE ISTO AQUI: Solicita a permissão assim que a tela abre
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Se for Android 12 ou superior
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            )
        }

        val api = RetrofitHelper.api
        val database = AppDataBase.getInstance(this)
        val orderDao = database.orderDAO()

        val factory = PaymentProcessViewModelFactory(
            api, orderDao,
            BluetoothDeviceHelper(this), BluetoothPrinterManager(this)
        )



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

                                if (!reciboJaImpresso) {
                                    // Marca como impresso IMEDIATAMENTE para evitar concorrência
                                    reciboJaImpresso = true

                                    try {
                                        Log.d(
                                            "PRINT_DEBUG",
                                            "2. Tentando carregar a imagem (logo)..."
                                        )
                                        val bitmap =
                                            BitmapFactory.decodeResource(resources, R.drawable.logo)

                                        if (bitmap == null) {
                                            Log.e(
                                                "PRINT_DEBUG",
                                                "🔴 ERRO FATAL: A imagem (R.drawable.logo) retornou null!"
                                            )
                                        } else {
                                            Log.d(
                                                "PRINT_DEBUG",
                                                "3. Imagem carregada. Redimensionando..."
                                            )
                                            val scaledBitmap = bitmap.scale(400, 300, false)

                                            Log.d(
                                                "PRINT_DEBUG",
                                                "4. Convertendo imagem para ESC/POS..."
                                            )
                                            // Se EscPosProcessor for um 'object', use EscPosProcessor.decodeBitmap
                                            // Se for classe, deixe com () como está no seu código.
                                            val imageBytes =
                                                EscPosProcessor().decodeBitmap(scaledBitmap)

                                            Log.d("PRINT_DEBUG", "5. Obtendo texto mockado...")
                                            val textBytes =
                                                EscPosProcessor().obterTextoMockEmBytes()

                                            Log.d("PRINT_DEBUG", "6. Juntando Imagem + Texto...")
                                            val reciboCompleto = imageBytes + textBytes

                                            Log.d(
                                                "PRINT_DEBUG",
                                                "7. Verificando permissão de Bluetooth..."
                                            )

                                            val temPermissao =
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                                    // Para Android 12+
                                                    androidx.core.content.ContextCompat.checkSelfPermission(
                                                        this@PaymentProcessActivity,
                                                        android.Manifest.permission.BLUETOOTH_CONNECT
                                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                                } else {
                                                    // Para Android 11 ou inferior
                                                    androidx.core.content.ContextCompat.checkSelfPermission(
                                                        this@PaymentProcessActivity,
                                                        android.Manifest.permission.BLUETOOTH
                                                    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                                }

                                            if (temPermissao) {
                                                Log.d(
                                                    "PRINT_DEBUG",
                                                    "8. Permissão OK! Enviando para o ViewModel imprimir..."
                                                )

                                                val impresso =
                                                    viewModel.imprimirRecibo(reciboCompleto)

                                                Log.d(
                                                    "PRINT_DEBUG",
                                                    "9. Retorno da impressão: $impresso"
                                                )
                                            } else {
                                                Log.e(
                                                    "PRINT_DEBUG",
                                                    "🔴 ERRO: Impressão cancelada (Sem permissão)."
                                                )
                                            }
                                        }
                                    } catch (e: Exception) {
                                        // Se der QUALQUER erro (falta de memória, erro de conversão, etc), vai cair aqui!
                                        Log.e(
                                            "PRINT_DEBUG",
                                            "🔴 CRASH SILENCIOSO CAPTURADO: Erro no bloco de impressão!",
                                            e
                                        )
                                    }

                                    Log.d(
                                        "PRINT_DEBUG",
                                        "10. Aguardando 3 segundos para trocar de tela..."
                                    )
                                    delay(3000)

                                    startActivity(
                                        Intent(
                                            this@PaymentProcessActivity,
                                            ConfirmationActivity::class.java
                                        )
                                    )

                                    finish()

                                } else {
                                    Log.d(
                                        "PRINT_DEBUG",
                                        "Tentativa ignorada: O recibo deste pedido já foi enviado para a impressora."
                                    )
                                }
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