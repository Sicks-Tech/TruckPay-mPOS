package com.jesse.sickstech.data.local.dao

import androidx.room.*
import com.jesse.sickstech.data.local.entity.AddonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddonDAO {

    // --- Inserção e Atualização ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddon(addon: AddonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddons(addons: List<AddonEntity>) : List<Long>

    @Update
    suspend fun updateAddon(addon: AddonEntity)

    // --- Exclusão ---

    @Delete
    suspend fun deleteAddon(addon: AddonEntity)

    /**
     * Remove todos os addons de uma loja específica.
     */
    @Query("DELETE FROM Addon WHERE store_id = :storeId")
    suspend fun deleteAddonsByStore(storeId: Int)

    // --- Consultas (Leitura) ---

    /**
     * Busca um addon específico pelo ID.
     */
    @Query("SELECT * FROM Addon WHERE addon_id = :addonId")
    suspend fun getAddonById(addonId: Int): AddonEntity?

    /**
     * Retorna todos os addons de uma loja específica.
     * Útil para listar opções de complementos no cardápio/catálogo.
     */
    @Query("SELECT * FROM Addon WHERE store_id = :storeId ORDER BY name ASC")
    fun getAddonsByStore(storeId: Int): Flow<List<AddonEntity>>

    /**
     * Busca addons pelo nome (útil para busca ou filtros).
     */
    @Query("SELECT * FROM Addon WHERE name LIKE '%' || :search || '%' AND store_id = :storeId")
    fun searchAddonsInStore(storeId: Int, search: String): Flow<List<AddonEntity>>
}