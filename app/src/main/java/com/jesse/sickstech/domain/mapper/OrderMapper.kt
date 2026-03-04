package com.jesse.sickstech.domain.mapper

import com.jesse.sickstech.domain.enums.OrderStatus

fun String.toOrderStatus(): OrderStatus {
    return when (this) {
        "PAID" -> OrderStatus.PAID
        "CANCELED" -> OrderStatus.CANCELED
        else -> OrderStatus.OPEN
    }
}