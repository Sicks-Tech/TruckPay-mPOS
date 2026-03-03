package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "order_item_addon",
    foreignKeys = [
        ForeignKey(
            entity = OrderItemEntity::class,
            parentColumns = ["order_item_id"],
            childColumns = ["order_item_id"],
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
        Index(value = ["order_item_id"]),
        Index(value = ["addon_id"])
    ]
)
data class OrderItemAddonEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_item_addon_id")
    val orderItemAddonId: Int = 0,

    @ColumnInfo(name = "order_item_id")
    val orderItemId: Int,

    @ColumnInfo(name = "addon_id")
    val addonId: Int,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "price_delta_cents")
    val priceDeltaCents: Int
)