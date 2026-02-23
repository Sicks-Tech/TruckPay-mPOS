package com.jesse.sickstech.data.local.dao

import androidx.room.*
import com.jesse.sickstech.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDAO {

    // --- Escrita ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Update
    suspend fun updatePayment(payment: PaymentEntity)

    // --- Consultas de Negócio ---

    /**
     * Busca os pagamentos de um pedido específico.
     * Útil caso o pedido aceite múltiplos métodos de pagamento (ex: parte em dinheiro, parte em PIX).
     */
    @Query("SELECT * FROM Payment WHERE order_id = :orderId")
    fun getPaymentsByOrderId(orderId: Int): Flow<List<PaymentEntity>>

    /**
     * Verifica o status do pagamento de um pedido de forma direta.
     */
    @Query("SELECT status FROM Payment WHERE order_id = :orderId LIMIT 1")
    fun getPaymentStatusByOrderId(orderId: Int): Flow<String?>

    // --- Consultas de Sincronização (Sync) ---

    /**
     * Busca pagamentos que ainda não foram sincronizados com o servidor.
     * Use isso no seu Worker ou Repository de sincronização.
     */
    @Query("SELECT * FROM Payment WHERE sync_status = 'PENDING'")
    suspend fun getUnsyncedPayments(): List<PaymentEntity>

    /**
     * Atualiza o status de sincronização de vários pagamentos de uma vez após o upload.
     */
    @Query("UPDATE Payment SET sync_status = :newStatus WHERE payment_id IN (:paymentIds)")
    suspend fun updateSyncStatus(paymentIds: List<Int>, newStatus: String)

    // --- Deleção ---

    @Query("DELETE FROM Payment WHERE order_id = :orderId")
    suspend fun deletePaymentsByOrderId(orderId: Int)
}