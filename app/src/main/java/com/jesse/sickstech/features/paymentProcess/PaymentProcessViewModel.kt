package com.jesse.sickstech.features.paymentProcess

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesse.sickstech.core.util.BluetoothDeviceHelper
import com.jesse.sickstech.data.api.Api
import com.jesse.sickstech.data.local.dao.OrderDAO
import com.jesse.sickstech.data.printer.BluetoothPrinterManager
import com.jesse.sickstech.data.printer.EscPosProcessor
import com.jesse.sickstech.domain.mapper.mapMpStatusToOrderStatus
import kotlinx.coroutines.launch
import java.util.Locale

class PaymentProcessViewModel(
    private val api: Api,
    private val orderDao: OrderDAO,
    private val bluetoothHelper: BluetoothDeviceHelper,
    private val printerManager: BluetoothPrinterManager
) : ViewModel() {

    val LINE_WIDTH = 32

    fun observeOrderByMpId(mpOrderId: String) =
        orderDao.observeByMpOrderId(mpOrderId)

    fun checkOrderStatus(orderId: String) {
        viewModelScope.launch {
            try {

                Log.d("STATUS", "Checking orderId: $orderId")

                val response = api.getOrder(orderId)

                Log.d("STATUS", "Response code: ${response.code()}")

                if (response.isSuccessful) {

                    val body = response.body()
                    Log.d("STATUS", "Body: $body")

                    body?.let { responseBody ->

                        val apiStatus = responseBody.status
                        val mappedStatus = mapMpStatusToOrderStatus(apiStatus)

                        Log.d("STATUS", "API status: $apiStatus")
                        Log.d("STATUS", "Mapped status: $mappedStatus")

                        val currentOrder = orderDao.getByMpOrderId(orderId)
                        Log.d("STATUS", "Current DB before update: $currentOrder")

                        currentOrder?.let { order ->
                            if (order.status != mappedStatus) {

                                order.status = mappedStatus
                                orderDao.update(order)

                                val after = orderDao.getByMpOrderId(orderId)
                                Log.d("STATUS", "After update DB: $after")
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("STATUS", "Error", e)
            }
        }
    }

    suspend fun getOrderByMpId(mpOrderId: String) =
        orderDao.getByMpOrderId(mpOrderId)


    // Nova função para imprimir (é 'suspend' para segurarmos a tela até terminar)
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    suspend fun imprimirRecibo(imageBytes: ByteArray): Boolean {
        return try {
            val device = bluetoothHelper.getImpressoraPareada()
            if (device != null) {
                // Tenta imprimir e retorna se deu certo ou não
                printerManager.printData(device, imageBytes)
            } else {
                Log.e("PRINTER", "Impressora não encontrada")
                false
            }
        } catch (e: Exception) {
            Log.e("PRINTER", "Erro ao tentar imprimir", e)
            false
        }
    }


    fun String.truncate(maxLength: Int): String {
        return if (this.length <= maxLength) this else this.substring(0, maxLength)
    }

    fun formatDuoLine(left: String, right: String, width: Int = LINE_WIDTH): String {
        val spaces = width - left.length - right.length
        return if (spaces > 0) {
            left + " ".repeat(spaces) + right
        } else {
            "$left $right"
        }
    }

    fun formatCurrency(value: Double): String {
        return String.format(Locale("pt", "BR"), "%.2f", value)
    }

    suspend fun gerarRecibo(orderId: Int): ByteArray {
        val order = orderDao.getFullOrder(orderId)
        val builder = StringBuilder()

        // --- CABEÇALHO ---
        builder.appendLine("Atendente: Jesse")
        builder.appendLine("Disp: SERVIDOR")
        builder.appendLine("-".repeat(LINE_WIDTH))


        builder.appendLine("Produto")
        // Encurtamos um pouco os títulos para caber bem nos 32 chars
        val rightTitles = formatDuoLine("Vl.un", "Vl.tot", 16)
        builder.appendLine(formatDuoLine("  Qtd Un", rightTitles))
        builder.appendLine("-".repeat(LINE_WIDTH))

        // --- ITENS ---
        order.items.forEach { item ->


            val prodName = " ${item.product.name}"
            builder.appendLine(prodName.truncate(LINE_WIDTH))


            val unitPrice = item.product.priceCents / 100.0
            val totalPrice = (item.item.quantity * item.product.priceCents) / 100.0

            val valUnitStr = formatCurrency(unitPrice)
            val valTotalStr = formatCurrency(totalPrice)


            val leftPart = "  ${item.item.quantity}xUN"
            val rightPart = "$valUnitStr=$valTotalStr"

            builder.appendLine(formatDuoLine(leftPart, rightPart))

            // Adicionais
            item.addonEntities.forEach { addon ->
                val addonName = " + Add ${addon.addonId} x${addon.quantity}"
                builder.appendLine(addonName.truncate(LINE_WIDTH))
            }
        }

        builder.appendLine("-".repeat(LINE_WIDTH))

        // --- TOTAL ---
        val total = order.order.totalCents / 100.0
        builder.appendLine(formatDuoLine("TOTAL:", "R$ " + formatCurrency(total)))

        builder.appendLine("\nObrigado!")

        return EscPosProcessor().textToBytes(builder.toString())
    }


}