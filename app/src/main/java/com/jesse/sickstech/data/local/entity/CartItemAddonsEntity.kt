package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "CartItemAddons",
    foreignKeys = [
        ForeignKey(
            entity = CartItemEntity::class,
            parentColumns = ["cart_item_id"],
            childColumns = ["cart_item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cart_item_id"])]
)
data class CartItemAddonsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "cart_item_id") val cartItemId: Long,
    @ColumnInfo(name = "addon_id") val addonId: Int,
    @ColumnInfo(name = "quantity") val quantity: Int
)