package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Addon",
    foreignKeys = [
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = ["store_id"],
            childColumns = ["store_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["store_id"])
    ]
)
data class AddonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "addon_id")
    val addonId: Int = 0,

    @ColumnInfo(name = "store_id")
    val storeId: Int = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "price_cents")
    val priceCents: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)