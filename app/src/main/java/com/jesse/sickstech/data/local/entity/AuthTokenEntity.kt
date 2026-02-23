package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "AuthToken" ,
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["account_id"],
            childColumns = ["account_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["account_id"])]
    )
data class AuthTokenEntity(
    @ColumnInfo(name = "account_id")
    @PrimaryKey
    val accountId: Int,

    @ColumnInfo(name = "token")
    val token: String,

    @ColumnInfo(name = "expires_at")
    val expiresAt: Long,

)
