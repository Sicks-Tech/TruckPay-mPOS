package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Transaction",
    foreignKeys = [
        ForeignKey(
            entity = PaymentEntity::class,
            parentColumns = ["payment_id"],
            childColumns = ["payment_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["payment_id"])
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val transactionId: Int = 0,

    @ColumnInfo(name = "payment_id")
    val paymentId: Int = 0,

    @ColumnInfo(name = "provider")
    val provider: String = "",

    @ColumnInfo(name = "transaction_type")
    val transactionType: String = "",

    @ColumnInfo(name = "nsu")
    val nsu: String = "",

    @ColumnInfo(name = "status")
    val status: String = "",

    @ColumnInfo(name = "amount_cents")
    val amountCents: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
