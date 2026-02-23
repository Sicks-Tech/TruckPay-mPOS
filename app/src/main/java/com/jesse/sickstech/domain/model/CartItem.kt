package com.jesse.sickstech.domain.model

import java.math.BigDecimal

//data class CartItem(
//    val id: Int,
//    val productId: Int,
//    val productName: String,
//    val productCode: String,
//    val productPrice: BigDecimal,
//    val quantity: Int,
//    // Futuramente você adicionará a lista de addons selecionados aqui
//    val addons: List<Addon> = emptyList()
//)

data class CartItem(
    val id: Int,
    val productId: Int,
    val productName: String,
    val productCode: String,
    val productPrice: BigDecimal,
    val quantity: Int,
    val selectedAddons: List<SelectedAddon> = emptyList(),
    val createdAt: Long
){
    // Cálculo dinâmico para o textViewPrecoTotal do seu Adapter
    val totalWithAddons: BigDecimal
        get() {
            val addonsSum = selectedAddons.fold(BigDecimal.ZERO) { acc, item ->
                acc + (item.addon.price * item.quantity.toBigDecimal())
            }
            return (productPrice + addonsSum).multiply(quantity.toBigDecimal())
        }

    val getToal: BigDecimal
        get() {
            return (productPrice * quantity.toBigDecimal())
        }
}