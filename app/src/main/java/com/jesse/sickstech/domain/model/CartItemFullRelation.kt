package com.jesse.sickstech.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.jesse.sickstech.data.local.entity.AddonEntity
import com.jesse.sickstech.data.local.entity.CartItemAddonsEntity

data class CartItemFullRelation(
    @Embedded
    val cartItemWithProduct: CartItemWithProduct, // Reutiliza a que você já tem!

    @Relation(
        entity = CartItemAddonsEntity::class,
        parentColumn = "cart_item_id", // ID do CartItemEntity
        entityColumn = "cart_item_id"  // ID na tabela de Addons do Carrinho
    )
    val addons: List<CartItemAddonWithDetails> // Aqui pegamos os detalhes dos addons
)

// Wrapper para pegar o nome e preço do Addon, não só o ID
data class CartItemAddonWithDetails(
    @Embedded val relation: CartItemAddonsEntity,
    @Relation(
        parentColumn = "addon_id",
        entityColumn = "addon_id"
    )
    val addonDetails: AddonEntity
)