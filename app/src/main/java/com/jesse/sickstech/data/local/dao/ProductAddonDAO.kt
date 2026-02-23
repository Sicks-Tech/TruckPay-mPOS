package com.jesse.sickstech.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jesse.sickstech.data.local.entity.AddonEntity
import com.jesse.sickstech.data.local.entity.ProductAddonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductAddonDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductAddon(productAddon: ProductAddonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductAddons(productAddons: List<ProductAddonEntity>) : List<Long>


    @Delete
    suspend fun deleteProductAddon(productAddon: ProductAddonEntity)

    /**
     * A "Mágica": Busca todos os detalhes dos adicionais vinculados a um produto.
     * Faz um JOIN entre ProductAddon e Addon para trazer nome, preço, etc.
     */
    @Query("""
        SELECT a.* FROM Addon a
        INNER JOIN ProductAddon pa ON a.addon_id = pa.addon_id
        WHERE pa.product_id = :productId
    """)
    fun getAddonsForProduct(productId: Int): Flow<List<AddonEntity>>

    /**
     * Remove todos os vínculos de adicionais de um produto específico.
     */
    @Query("DELETE FROM ProductAddon WHERE product_id = :productId")
    suspend fun removeAllAddonsFromProduct(productId: Int)
}