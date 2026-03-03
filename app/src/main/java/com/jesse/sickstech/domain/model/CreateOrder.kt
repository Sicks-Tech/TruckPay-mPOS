package com.jesse.sickstech.domain.model

import java.math.BigDecimal

data class CreateOrder(
    val orderId: Int,
    val total: BigDecimal
)
