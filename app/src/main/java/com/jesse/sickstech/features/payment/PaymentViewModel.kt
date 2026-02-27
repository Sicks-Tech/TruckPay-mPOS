package com.jesse.sickstech.features.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesse.sickstech.data.repository.OrderRepository
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

    suspend fun createOrder(accountId: Int, storeId: Int, total: BigDecimal): Int {
        val id = orderRepository.createOrderFromCart(accountId, storeId, total)

        // O LOG IDEAL:
        Log.d("OrderFlow", "✅ Pedido Criado com Sucesso! ID: $id | Total: R$ $total")

        return id
    }
}