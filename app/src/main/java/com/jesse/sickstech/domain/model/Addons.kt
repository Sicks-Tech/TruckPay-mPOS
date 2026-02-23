package com.jesse.sickstech.domain.model

import java.math.BigDecimal

data class Addon(
    val id: Int,
    val name: String,
    val price: BigDecimal
)

data class SelectedAddon(
    val addon: Addon,
    val quantity: Int = 1
)

data class AddonsState(
    val productId: Int = 0,
    val titulo: String = "",
    val precoBase: BigDecimal = BigDecimal.ZERO,
    val addons: List<SelectedAddon> = emptyList(),
    val addonsTotal: BigDecimal = BigDecimal.ZERO,
    val total: BigDecimal = BigDecimal.ZERO
)