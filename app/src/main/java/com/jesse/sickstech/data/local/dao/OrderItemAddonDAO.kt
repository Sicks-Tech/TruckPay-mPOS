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

    // --- Consultas (Leitura) ---

    /**
     * Busca todos os adicionais de um item específico do carrinho.
     * Essencial para listar "X-Tudo + Bacon + Ovo" na UI.
     */
    @Query("SELECT * FROM OrderItemAddon WHERE cart_item_id = :cartItemId")
    fun getAddonsByCartItem(cartItemId: Int): Flow<List<OrderItemAddonEntity>>

    /**
     * Versão síncrona para cálculos rápidos ou Worker.
     */
    @Query("SELECT * FROM OrderItemAddon WHERE cart_item_id = :cartItemId")
    suspend fun getAddonsByCartItemSync(cartItemId: Int): List<OrderItemAddonEntity>

    /**
     * Calcula o valor total apenas dos adicionais de um item.
     */
    @Query("SELECT SUM(quantity * price_delta_cents) FROM OrderItemAddon WHERE cart_item_id = :cartItemId")
    fun getTotalAddonsPriceByItem(cartItemId: Int): Flow<Int?>

    // --- Limpeza ---

    /**
     * Remove todos os adicionais de um item.
     * Útil se o usuário quiser "limpar customizações" do produto.
     */
    @Query("DELETE FROM OrderItemAddon WHERE cart_item_id = :cartItemId")
    suspend fun deleteAllAddonsFromItem(cartItemId: Int)
}