package com.jesse.sickstech.features.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesse.sickstech.data.api.RetrofitHelper.api
import com.jesse.sickstech.data.model.order.Config
import com.jesse.sickstech.data.model.order.CreateOrderResult
import com.jesse.sickstech.data.model.order.OrderRequest
import com.jesse.sickstech.data.model.order.Payment
import com.jesse.sickstech.data.model.order.PaymentMethodConfig
import com.jesse.sickstech.data.model.order.PointConfig
import com.jesse.sickstech.data.model.order.Transactions
import com.jesse.sickstech.data.model.pos.OrderConfig
import com.jesse.sickstech.data.repository.OrderRepository
import com.jesse.sickstech.domain.model.CreateOrder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal

class PaymentViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    // Transformamos o Flow do repositório em um StateFlow que a UI consegue ler
    val cartTotal: StateFlow<BigDecimal> = orderRepository.getCartTotal(accountId = 1)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BigDecimal.ZERO
        )

    suspend fun pagar( accountId: Int,
   storeId: Int, paymentType: String): CreateOrderResult {
        return orderRepository.createOrderFromCart(accountId , storeId, paymentType)
    }
}