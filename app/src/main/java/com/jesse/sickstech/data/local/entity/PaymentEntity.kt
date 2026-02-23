package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "Payment",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["order_id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["order_id"])
    ]
)
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_id")
    val paymentId: Int = 0,

    @ColumnInfo(name = "order_id")
    val orderId: Int = 0,

    @ColumnInfo(name = "payment_method")
    val paymentMethod: String = "",

    @ColumnInfo(name = "amount_cents")
    val amountCents: Int = 0,

    @ColumnInfo(name = "status")
    val status: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "sync_status")
    val syncStatus: String = ""
)
