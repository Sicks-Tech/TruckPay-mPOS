package com.jesse.sickstech.data.repository.cart

import com.jesse.sickstech.features.cart.Cart

class CartRepository {
    fun getCardItens(): List<Cart>{
        return listOf(
            Cart(1, "7892840800567", "Hamburguer Super Truck", 30.00),
            Cart(2, "7891234567890", "Batata Frita Média", 12.50),
            Cart(3, "7890987654321", "Refrigerante Cola 600ml", 8.99),
            Cart(4, "7891112223334", "Combo Família", 89.90),
            Cart(5, "7895556667778", "Sobremesa Doce de Leite", 6.75)
        )
    }
}