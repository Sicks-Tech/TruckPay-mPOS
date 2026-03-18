package com.jesse.sickstech.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.jesse.sickstech.data.local.entity.OrderEntity
import com.jesse.sickstech.data.local.entity.OrderItemAddonEntity
import com.jesse.sickstech.data.local.entity.OrderItemEntity
import com.jesse.sickstech.data.local.entity.ProductEntity

data class OrderWithItems(
    @Embedded
    val order: OrderEntity,

    @Relation(
        entity = OrderItemEntity::class, // CORREÇÃO 1: Diga explicitamente ao Room qual tabela consultar
        parentColumn = "order_id",
        entityColumn = "order_id"
    )
    val items: List<OrderItemWithAddons>
)

data class OrderItemWithAddons(
    @Embedded
    val item: OrderItemEntity,

    @Relation(
        entity = ProductEntity::class, // Declarar a entidade explicitamente é uma boa prática
        parentColumn = "product_id",
        entityColumn = "product_id"
    )
    val product: ProductEntity,

    // CORREÇÃO 2: O Room precisa consultar uma Entity (ou uma classe de relação aninhada), não um objeto simples (POJO).
    @Relation(
        entity = OrderItemAddonEntity::class,
        parentColumn = "order_item_id", // <-- ATUALIZE ISSO para a chave primária real da sua OrderItemEntity
        entityColumn = "order_item_id"  // <-- ATUALIZE ISSO para a chave estrangeira na sua OrderItemAddonEntity
    )
    val addonEntities: List<OrderItemAddonEntity>
)

// Você pode manter essa classe aqui para usar na sua camada de UI/ViewModel,
// mas o Room não tentará mais preenchê-la diretamente do banco.
data class AddonItem(
    val name: String,
    val quantity: Int,
    val priceCents: Int
)