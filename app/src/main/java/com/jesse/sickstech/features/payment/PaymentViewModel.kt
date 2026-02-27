package com.jesse.sickstech.features.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesse.sickstech.data.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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
}