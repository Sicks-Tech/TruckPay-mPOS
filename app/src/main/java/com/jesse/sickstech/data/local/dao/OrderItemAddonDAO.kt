package com.jesse.sickstech.data.local.dao

import androidx.room.*
import com.jesse.sickstech.data.local.entity.OrderItemAddonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemAddonDAO {

    // --- Escrita ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddonToItem(orderItemAddon: OrderItemAddonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddonsToItem(orderItemAddons: List<OrderItemAddonEntity>)

    @Update
    suspend fun updateAddonInItem(orderItemAddon: OrderItemAddonEntity)

    @Delete
    suspend fun deleteAddonFromItem(orderItemAddon: OrderItemAddonEntity)

    // --- Consultas ---

    /**
     * Busca todos os adicionais de um item específico do PEDIDO.
     */
    @Query("SELECT * FROM order_item_addon WHERE order_item_id = :orderItemId")
    fun getAddonsByOrderItem(orderItemId: Int): Flow<List<OrderItemAddonEntity>>

    /**
     * Versão síncrona.
     */
    @Query("SELECT * FROM order_item_addon WHERE order_item_id = :orderItemId")
    suspend fun getAddonsByOrderItemSync(orderItemId: Int): List<OrderItemAddonEntity>

    /**
     * Calcula o valor total dos adicionais de um item do pedido.
     */
    @Query("""
        SELECT SUM(quantity * price_delta_cents)
        FROM order_item_addon
        WHERE order_item_id = :orderItemId
    """)
    fun getTotalAddonsPriceByOrderItem(orderItemId: Int): Flow<Int?>

    // --- Limpeza ---

    /**
     * Remove todos os adicionais de um item do pedido.
     */
    @Query("DELETE FROM order_item_addon WHERE order_item_id = :orderItemId")
    suspend fun deleteAllAddonsFromOrderItem(orderItemId: Int)
}