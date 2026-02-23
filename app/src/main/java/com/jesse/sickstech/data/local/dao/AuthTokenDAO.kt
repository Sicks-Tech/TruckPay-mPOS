package com.jesse.sickstech.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jesse.sickstech.data.local.entity.AuthTokenEntity

@Dao
interface AuthTokenDAO {
    // salva / atualiza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(token: AuthTokenEntity)

    // pega o token ativo
    @Query("SELECT * FROM AuthToken LIMIT 1")
    suspend fun getToken(): AuthTokenEntity?

    // remove token (logout)
    @Query("DELETE FROM AuthToken")
    suspend fun clearToken()

    // checa se token expirou
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM AuthToken 
            WHERE expires_at > :currentTime
        )
    """)
    suspend fun hasValidToken(currentTime: Long): Boolean
}