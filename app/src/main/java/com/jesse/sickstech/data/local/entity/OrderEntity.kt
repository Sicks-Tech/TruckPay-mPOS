package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jesse.sickstech.domain.enums.OrderStatus
import com.jesse.sickstech.domain.enums.SyncStatus

@Entity(
    tableName = "Order",
    foreignKeys = [
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = ["store_id"],
            childColumns = ["store_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["account_id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["store_id"]),
        Index(value = ["account_id"])
    ]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    val orderId: Int = 0,

    @ColumnInfo(name = "mp_order_id")
    var mpOrderId: String? = null,

    @ColumnInfo(name = "store_id")
    val storeId: Int = 0,

    @ColumnInfo(name = "account_id")
    val accountId: Int = 0,

    @ColumnInfo(name = "total_cents")
    val totalCents: Int = 0,

    @ColumnInfo(name = "status")
    var status: OrderStatus = OrderStatus.OPEN,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.LOCAL
)
