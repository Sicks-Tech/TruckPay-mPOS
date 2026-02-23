package com.jesse.sickstech.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Menu(
    val id: Int,
    val imagem: Int,
    val titulo: String,
    val precoCents: BigDecimal
) : Parcelable