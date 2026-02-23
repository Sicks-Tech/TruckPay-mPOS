package com.jesse.sickstech.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesse.sickstech.data.repository.OrderRepository
import com.jesse.sickstech.domain.model.CartItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.collections.emptyList

class CartViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {
    val cartItems: StateFlow<List<CartItem>> = orderRepository.getCartItems(accountId = 1)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList<CartItem>()
        )

    val totalCart: StateFlow<BigDecimal> = cartItems
        .map { items ->
            items.sumOf { it.totalWithAddons }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BigDecimal.ZERO
        )




    fun clearCart() {
        viewModelScope.launch {
            orderRepository.clearCart(accountId = 1)
        }
    }

}