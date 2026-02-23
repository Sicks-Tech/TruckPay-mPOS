package com.jesse.sickstech.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jesse.sickstech.data.local.entity.ProductEntity

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>) : List<Long>

    @Update
    suspend fun update(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("""
        SELECT * FROM Product 
        WHERE store_id = :storeId 
        AND active = 1
    """)
    suspend fun getByStore(storeId: Int): List<ProductEntity>

    @Query("SELECT * FROM Product WHERE product_id = :productId")
    suspend fun getById(productId: Int): ProductEntity?

    @Query("DELETE FROM Product")
    suspend fun clear()
}