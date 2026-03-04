package com.jesse.sickstech.domain.mapper

import com.jesse.sickstech.domain.enums.OrderStatus

fun mapMpStatusToOrderStatus(mpStatus: String): OrderStatus {
    return when (mpStatus.lowercase()) {
        "pending" -> OrderStatus.PENDING
        "processed" -> OrderStatus.PROCESSED
        "canceled" -> OrderStatus.CANCELED
        "failed" -> OrderStatus.FAILED
        else -> OrderStatus.OPEN
    }
}