package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "Product",
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
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val productId: Int = 0,

    @ColumnInfo(name = "store_id")
    val storeId: Int = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "product_code")
    val productCode: String = "",

    @ColumnInfo(name = "price_cents")
    val priceCents: Int = 0,

    @ColumnInfo(name = "image_url")
    val imageUrl: Int,

    @ColumnInfo(name = "active")
    val active: Boolean = true
)
