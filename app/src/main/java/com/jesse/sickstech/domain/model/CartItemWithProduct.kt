package com.jesse.sickstech.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.jesse.sickstech.data.local.entity.CartItemEntity
import com.jesse.sickstech.data.local.entity.ProductEntity

data class CartItemWithProduct(
    @Embedded val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_id"
    )
    val product: ProductEntity
)
