package com.jesse.sickstech.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jesse.sickstech.data.local.entity.CartItemAddonsEntity

@Dao
interface CartItemAddonsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(addons: List<CartItemAddonsEntity>)

    @Query("SELECT * FROM CartItemAddons WHERE cart_item_id = :cartItemId")
    suspend fun getAddonsByCartItem(cartItemId: Int): List<CartItemAddonsEntity>
}