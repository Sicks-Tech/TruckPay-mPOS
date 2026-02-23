package com.jesse.sickstech.core.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    private val formatter = NumberFormat.getCurrencyInstance(
        Locale("pt", "BR")
    )

    fun format(value: BigDecimal): String {
        return formatter.format(value)
    }

    fun formatFromCents(cents: Int): String {
        val value = BigDecimal(cents).divide(BigDecimal(100))
        return formatter.format(value)
    }
}