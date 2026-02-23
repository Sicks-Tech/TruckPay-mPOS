package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jesse.sickstech.domain.enums.AccountRole

@Entity(
    tableName = "Account",
    foreignKeys = [
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = ["store_id"],
            childColumns = ["store_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["store_id"])]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    val accountId: Int,

    @ColumnInfo(name = "role")
    val role: AccountRole,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "store_id")
    val storeId: Int
)