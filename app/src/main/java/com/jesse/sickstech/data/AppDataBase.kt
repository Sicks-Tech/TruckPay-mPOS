package com.jesse.sickstech.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jesse.sickstech.data.local.dao.AccountDAO
import com.jesse.sickstech.data.local.dao.AddonDAO
import com.jesse.sickstech.data.local.dao.AuthTokenDAO
import com.jesse.sickstech.data.local.dao.CartItemAddonsDAO
import com.jesse.sickstech.data.local.dao.CartItemDAO
import com.jesse.sickstech.data.local.dao.OrderDAO
import com.jesse.sickstech.data.local.dao.OrderItemAddonDAO
import com.jesse.sickstech.data.local.dao.OrderItemDAO
import com.jesse.sickstech.data.local.dao.PaymentDAO
import com.jesse.sickstech.data.local.dao.ProductAddonDAO
import com.jesse.sickstech.data.local.dao.ProductDAO
import com.jesse.sickstech.data.local.dao.StoreDAO
import com.jesse.sickstech.data.local.dao.TransactionDAO
import com.jesse.sickstech.data.local.entity.AccountEntity
import com.jesse.sickstech.data.local.entity.AddonEntity
import com.jesse.sickstech.data.local.entity.AuthTokenEntity
import com.jesse.sickstech.data.local.entity.CartItemAddonsEntity
import com.jesse.sickstech.data.local.entity.CartItemEntity
import com.jesse.sickstech.data.local.entity.OrderEntity
import com.jesse.sickstech.data.local.entity.OrderItemAddonEntity
import com.jesse.sickstech.data.local.entity.OrderItemEntity
import com.jesse.sickstech.data.local.entity.PaymentEntity
import com.jesse.sickstech.data.local.entity.ProductAddonEntity
import com.jesse.sickstech.data.local.entity.ProductEntity
import com.jesse.sickstech.data.local.entity.StoreEntity
import com.jesse.sickstech.data.local.entity.TransactionEntity
import com.jesse.sickstech.data.local.seed.DatabaseSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        AccountEntity::class,
        AddonEntity::class,
        AuthTokenEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemAddonEntity::class ,
        OrderItemEntity::class,
        PaymentEntity::class,
        ProductAddonEntity::class,
        ProductEntity::class,
        StoreEntity::class,
        TransactionEntity::class,
        CartItemAddonsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    // DAOs declared in the same order as the provided image
    abstract fun accountDAO(): AccountDAO
    abstract fun addonDAO(): AddonDAO
    abstract fun authTokenDAO(): AuthTokenDAO
    abstract fun cartItemDAO(): CartItemDAO
    abstract fun orderDAO(): OrderDAO
    abstract fun orderItemAddonDAO(): OrderItemAddonDAO
    abstract fun orderItemDAO(): OrderItemDAO
    abstract fun paymentDAO(): PaymentDAO
    abstract fun productAddonDAO(): ProductAddonDAO
    abstract fun productDAO(): ProductDAO
    abstract fun storeDAO(): StoreDAO
    abstract fun transactionDAO(): TransactionDAO
    abstract fun cartItemAddonsDAO(): CartItemAddonsDAO



    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "truckpay_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    DatabaseSeeder(
                        database.storeDAO() ,
                        database.productDAO(),
                    database.addonDAO(),
                        database.productAddonDAO(),
                        database.accountDAO(),
                        database.cartItemAddonsDAO()
                    ).seed()
                    Log.d("SEED", "Seed rodou")
                }
            }
        }
    }

}

