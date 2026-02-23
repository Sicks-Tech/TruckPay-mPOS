// File: `app/src/main/java/com/jesse/sickstech/features/menu/MenuViewModelFactory.kt`
package com.jesse.sickstech.features.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jesse.sickstech.data.repository.OrderRepository
import com.jesse.sickstech.data.repository.ShopRepository

class MenuViewModelFactory(
    private val shopRepository: ShopRepository,
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel(shopRepository, orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}