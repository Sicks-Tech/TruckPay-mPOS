package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "OrderItemAddon",
    foreignKeys = [
        ForeignKey(
            entity = CartItemEntity::class,
            parentColumns = ["cart_item_id"],
            childColumns = ["cart_item_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AddonEntity::class,
            parentColumns = ["addon_id"],
            childColumns = ["addon_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["cart_item_id"]),
        Index(value = ["addon_id"])
    ]
)
data class OrderItemAddonEntity(
    @PrimaryKey
    @ColumnInfo(name = "cart_item_addon_id")
    val cartItemAddonId: Int = 0,

    @ColumnInfo(name = "cart_item_id")
    val cartItemId: Int = 0,

    @ColumnInfo(name = "addon_id")
    val addonId: Int = 0,

    @ColumnInfo(name = "quantity")
    val quantity: Int = 1,

    @ColumnInfo(name = "price_delta_cents")
    val priceDeltaCents: Int
)