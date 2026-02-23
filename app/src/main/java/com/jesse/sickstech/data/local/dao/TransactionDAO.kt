package com.jesse.sickstech.data.local.dao

import androidx.room.*
import com.jesse.sickstech.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {

    // --- Registro ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    // --- Consultas ---

    /**
     * Busca os detalhes da transação vinculada a um pagamento.
     * Útil para exibir o comprovante técnico (NSU, Operadora) na UI.
     */
    @Query("SELECT * FROM `Transaction` WHERE payment_id = :paymentId")
    fun getTransactionsByPayment(paymentId: Int): Flow<List<TransactionEntity>>

    /**
     * Busca uma transação específica pelo NSU.
     * Vital para processos de conciliação ou busca de transações duplicadas.
     */
    @Query("SELECT * FROM `Transaction` WHERE nsu = :nsu LIMIT 1")
    suspend fun getTransactionByNsu(nsu: String): TransactionEntity?

    /**
     * Retorna o histórico de transações por período.
     * Útil para relatórios de fechamento de caixa.
     */
    @Query("SELECT * FROM `Transaction` WHERE created_at BETWEEN :startTime AND :endTime")
    fun getTransactionsByPeriod(startTime: Long, endTime: Long): Flow<List<TransactionEntity>>

    // --- Utilitários ---

    @Query("SELECT SUM(amount_cents) FROM `Transaction` WHERE status = 'SUCCESS'")
    fun getTotalSuccessfulTransactions(): Flow<Int?>

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}