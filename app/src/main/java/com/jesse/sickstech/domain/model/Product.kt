package com.jesse.sickstech.domain.model

import java.math.BigDecimal

data class Product(
    val id: Int,
    val storeId: Int,
    val name: String,
    val description: String,
    val code: String,
    val price: BigDecimal,
    val imageUrl: Int,
    val isActive: Boolean
)