package com.jesse.sickstech.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jesse.sickstech.data.local.entity.AccountEntity

@Dao
interface AccountDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)

    @Query("SELECT * FROM Account WHERE account_id = :accountId LIMIT 1")
    suspend fun getAccountById(accountId: Int): AccountEntity

    @Query("DELETE FROM Account")
    suspend fun deleteAllAccount()

}