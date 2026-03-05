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
import com.jesse.sickstech.domain.mapper.mapMpStatusToOrderStatus
import kotlinx.coroutines.launch

class PaymentProcessViewModel(
    private val api: Api,
    private val orderDao: OrderDAO,
    private val bluetoothHelper: BluetoothDeviceHelper,
    private val printerManager: BluetoothPrinterManager
) : ViewModel() {

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
}