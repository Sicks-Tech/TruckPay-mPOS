package com.jesse.sickstech.data.repository

import android.content.Context
import com.jesse.sickstech.R
import com.jesse.sickstech.data.AppDataBase
import com.jesse.sickstech.data.local.dao.ProductDAO
import com.jesse.sickstech.data.local.entity.AddonEntity
import com.jesse.sickstech.data.local.entity.ProductEntity
import com.jesse.sickstech.domain.model.Addon
import com.jesse.sickstech.domain.model.Menu
import com.jesse.sickstech.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal


/*
deve lhe dar com product/addon/store
 */
class ShopRepository private constructor(context: Context) {
    private val database = AppDataBase.getInstance(context.applicationContext)

    private val productDAO = database.productDAO()
    private val productAddonDAO = database.productAddonDAO()
    private val storeDAO = database.storeDAO()


    companion object {
        @Volatile
        private var instance: ShopRepository? = null

        fun getInstance(context: Context): ShopRepository {
            return instance ?: synchronized(this) {
                instance ?: ShopRepository(context).also { instance = it }

            }
        }
    }


    // funcao getProducts, pra pegar o produto pelo id da loja
    suspend fun getProducts(storeId: Int): List<ProductEntity> {
        return productDAO.getByStore(storeId)
    }

    suspend fun getProductsMenu(storeId: Int): List<Product> {
        return productDAO.getByStore(storeId)
            .map { entity ->
                entity.toDomain()
            }
    }

    suspend fun getAddons(productId: Int): Flow<List<Addon>> {
        return productAddonDAO.getAddonsForProduct(productId)
            .map { entityList ->
                entityList.map { it.toDomain() }
            }
    }

}

private fun ProductEntity.toDomain(): Product {
    return Product(
        id = productId,
        storeId = storeId,
        name = name,
        description = description,
        code = productCode,
        price = priceCents.toBigDecimal().movePointLeft(2),
        imageUrl = imageUrl,
        isActive = active
    )
}

private fun AddonEntity.toDomain(): Addon {
    return Addon(
        id = addonId,
        name = name,
        price = priceCents.toBigDecimal().movePointLeft(2)
    )
}

