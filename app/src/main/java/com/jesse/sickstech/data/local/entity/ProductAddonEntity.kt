package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "ProductAddon",
    primaryKeys = ["product_id", "addon_id"] // Chave composta costuma ser melhor aqui
)
data class ProductAddonEntity(
    @ColumnInfo(name = "product_id")
    val productId: Int,

    @ColumnInfo(name = "addon_id")
    val addonId: Int
)
