package com.jesse.sickstech.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
@Entity(tableName = "Store")
class StoreEntity {
    @PrimaryKey
    @field:ColumnInfo(name = "store_id")
    var storeId: Int = 0
        get() = field
        set(value) { field = value }

    @field:ColumnInfo(name = "cnpj")
    var cnpj: String = ""
        get() = field
        set(value) { field = value }

    @field:ColumnInfo(name = "nome")
    var nome: String = ""
        get() = field
        set(value) { field = value }

    @field:ColumnInfo(name = "email")
    var email: String = ""
        get() = field
        set(value) { field = value }

    @field:ColumnInfo(name = "endereco")
    var endereco: String = ""
        get() = field
        set(value) { field = value }

    @field:ColumnInfo(name = "localizacao")
    var localizacao: String = ""
        get() = field
        set(value) { field = value }

    @field:ColumnInfo(name = "telefone")
    var telefone: String = ""
        get() = field
        set(value) { field = value }

    @field:ColumnInfo(name = "created_at")
    var createdAt: Long = System.currentTimeMillis()
        get() = field
        set(value) { field = value }
}

*/


@Entity(tableName = "Store")
data class StoreEntity(
    @PrimaryKey
    @ColumnInfo(name = "store_id")
    val storeId: Int,

    @ColumnInfo(name = "cnpj")
    val cnpj: String,

    @ColumnInfo(name = "nome")
    val nome: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "endereco")
    val endereco: String,

    @ColumnInfo(name = "localizacao")
    val localizacao: String,

    @ColumnInfo(name = "telefone")
    val telefone: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

