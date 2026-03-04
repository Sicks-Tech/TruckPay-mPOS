package com.jesse.sickstech.features.paymentProcess

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesse.sickstech.data.api.Api
import com.jesse.sickstech.data.local.dao.OrderDAO
import com.jesse.sickstech.domain.mapper.mapMpStatusToOrderStatus
import kotlinx.coroutines.launch

class PaymentProcessViewModel(
    private val api: Api,
    private val orderDao: OrderDAO
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
}