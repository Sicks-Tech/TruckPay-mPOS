package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "OrderItem",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["order_id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["order_id"]),
        Index(value = ["product_id"])
    ]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_item_id")
    val orderItemId: Int = 0,

    @ColumnInfo(name = "order_id")
    val orderId: Int = 0,

    @ColumnInfo(name = "product_id")
    val productId: Int = 0,

    @ColumnInfo(name = "quantity")
    val quantity: Int = 1,

    @ColumnInfo(name = "unit_price_cents")
    val unitPriceCents: Int = 0
)