package com.jesse.sickstech.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.jesse.sickstech.data.local.entity.CartItemEntity
import com.jesse.sickstech.domain.model.CartItem
import com.jesse.sickstech.domain.model.CartItemFullRelation
import com.jesse.sickstech.domain.model.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cartItem: CartItemEntity) : Long

    @Query("""
        UPDATE CartItem 
        SET quantity = quantity + :delta 
        WHERE account_id = :accountId 
        AND product_id = :productId
    """)
    suspend fun updateQuantity(
        accountId: Int,
        productId: Int,
        delta: Int
    )

    @Query("""
        SELECT * FROM CartItem 
        WHERE account_id = :accountId
        ORDER BY created_at ASC
    """)
    fun getByAccount(accountId: Int): Flow<List<CartItemEntity>>

    @Query("""
        DELETE FROM CartItem 
        WHERE account_id = :accountId
    """)
    suspend fun clearCart(accountId: Int)

    @Query("""
        DELETE FROM CartItem 
        WHERE account_id = :accountId 
        AND product_id = :productId
    """)
    suspend fun removeProduct(
        accountId: Int,
        productId: Int
    )

    @Transaction // Necessário para objetos com @Relation
    @Query("SELECT * FROM CartItem WHERE account_id = :accountId")
    fun getCartWithProduct(accountId: Int): Flow<List<CartItemWithProduct>>

    @Transaction
    @Query("SELECT * FROM CartItem WHERE account_id = :accountId")
    fun getCartFullDetails(accountId: Int): Flow<List<CartItemFullRelation>>
}