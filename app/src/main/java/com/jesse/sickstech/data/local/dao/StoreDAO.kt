package com.jesse.sickstech.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jesse.sickstech.data.local.entity.StoreEntity

@Dao
interface StoreDAO {
    // criar a loja
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: StoreEntity)

    // editar loja
    @Update
    suspend fun updateStore(store: StoreEntity)

    // deletar loja
    @Delete
    suspend fun deleteStore(store: StoreEntity)

    @Query("SELECT * FROM Store WHERE store_id = :storeId")
    suspend fun getStoreById(storeId: Int): StoreEntity?

    @Query("DELETE FROM Store")
    suspend fun deleteStore()

}