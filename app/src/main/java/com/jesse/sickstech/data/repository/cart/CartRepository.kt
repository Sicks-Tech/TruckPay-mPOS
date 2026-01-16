package com.jesse.sickstech.data.repository.cart

import com.jesse.sickstech.features.cart.CartItem

class CartRepository {
    fun getCardItens(): List<CartItem>{
        return listOf(
            CartItem(1, "7892840800567", "Hamburguer Super Truck", 30.00),
            CartItem(2, "7891234567890", "Batata Frita Média", 12.50),
            CartItem(3, "7890987654321", "Refrigerante Cola 600ml", 8.99),
            CartItem(4, "7891112223334", "Combo Família", 89.90),
            CartItem(5, "7895556667778", "Sobremesa Doce de Leite", 6.75)
        )
    }
}