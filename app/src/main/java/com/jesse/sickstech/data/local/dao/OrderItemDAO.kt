package com.jesse.sickstech.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jesse.sickstech.data.local.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemDAO {

    // --- Escrita ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<OrderItemEntity>)

    @Update
    suspend fun updateOrderItem(orderItem: OrderItemEntity)

    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItemEntity)

    // --- Leitura ---

    /**
     * Busca todos os itens de um pedido específico.
     * Útil para exibir os detalhes de uma compra.
     */
    @Query("SELECT * FROM OrderItem WHERE order_id = :orderId")
    fun getItemsByOrderId(orderId: Int): Flow<List<OrderItemEntity>>

    /**
     * Remove todos os itens de um pedido.
     * (Embora o CASCADE já trate isso se o pedido for deletado,
     * isso é útil para "limpar" um carrinho sem deletar o pedido pai).
     */
    @Query("SELECT * FROM OrderItem WHERE order_id = :orderId")
    suspend fun getItemsByOrderIdSync(orderId: Int): List<OrderItemEntity>

    /**
     * Calcula o valor total do pedido diretamente no SQL.
     * Ótimo para performance, evitando trazer tudo para a memória.
     */
    @Query("SELECT SUM(quantity * unit_price_cents) FROM OrderItem WHERE order_id = :orderId")
    fun getTotalPriceByOrderId(orderId: Int): Flow<Int?>

    /**
     * Deleta todos os itens vinculados a um pedido específico.
     */
    @Query("DELETE FROM OrderItem WHERE order_id = :orderId")
    suspend fun deleteItemsByOrderId(orderId: Int)
}