package com.jesse.sickstech.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jesse.sickstech.data.local.entity.OrderEntity

@Dao
interface OrderDAO {

    // cria pedido (abre pedido)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity): Long

    // atualiza pedido (status, total, sync)
    @Update
    suspend fun update(order: OrderEntity)

    // pega pedido ativo (ex: ABERTO)
    @Query("""
        SELECT * FROM `Order`
        WHERE account_id = :accountId
        AND status = :status
        LIMIT 1
    """)
    suspend fun getOrderByStatus(
        accountId: Int,
        status: String
    ): OrderEntity?

    // histórico de pedidos
    @Query("""
        SELECT * FROM `Order`
        WHERE account_id = :accountId
        ORDER BY created_at DESC
    """)
    suspend fun getOrdersByAccount(accountId: Int): List<OrderEntity>

    // pedidos pendentes de sync
    @Query("""
        SELECT * FROM `Order`
        WHERE sync_status != 'SYNCED'
    """)
    suspend fun getPendingSync(): List<OrderEntity>

    // limpa tudo (reset local)
    @Query("DELETE FROM `Order`")
    suspend fun clear()
}
