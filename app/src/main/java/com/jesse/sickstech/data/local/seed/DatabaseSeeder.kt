package com.jesse.sickstech.data.local.seed

import android.util.Log
import com.jesse.sickstech.R
import com.jesse.sickstech.data.local.dao.AccountDAO
import com.jesse.sickstech.data.local.dao.AddonDAO
import com.jesse.sickstech.data.local.dao.CartItemAddonsDAO
import com.jesse.sickstech.data.local.dao.ProductAddonDAO
import com.jesse.sickstech.data.local.dao.ProductDAO
import com.jesse.sickstech.data.local.dao.StoreDAO
import com.jesse.sickstech.data.local.entity.AccountEntity
import com.jesse.sickstech.data.local.entity.AddonEntity
import com.jesse.sickstech.data.local.entity.ProductAddonEntity
import com.jesse.sickstech.data.local.entity.ProductEntity
import com.jesse.sickstech.data.local.entity.StoreEntity
import com.jesse.sickstech.domain.enums.AccountRole

class DatabaseSeeder(
    private val storeDAO: StoreDAO,
    private val productDAO: ProductDAO,
    private val addonDAO: AddonDAO,
    private val productAddonDAO: ProductAddonDAO,
    private val accountDAO: AccountDAO,
    cartItemAddonsDAO: CartItemAddonsDAO
) {

    suspend fun seed() {

//        // Evita duplicar seed
        if (storeDAO.getStoreById(1) != null) return

        // Criar Store
        val store = StoreEntity(
            storeId = 1, // pode deixar fixo já que é seed
            nome = "Super Truck",
            cnpj = "",
            email = "sickstech@gmail.com",
            endereco = "Rua teste 00",
            localizacao = "São Paulo",
            telefone = "11999999999"
        )

        storeDAO.insertStore(store)

        accountDAO.insertAccount(
            AccountEntity(
                accountId = 1,
                role = AccountRole.ADMIN,
                storeId = store.storeId
            )
        )

        //  Products ligados à Store
        val productIds = productDAO.insertAll(
            listOf(
                ProductEntity(
                    storeId = store.storeId,
                    name = "Hamburguer Super Truck",
                    description = "Pão, carne, queijo",
                    productCode = "HT01",
                    priceCents = 3000,
                    imageUrl = R.drawable.hamburguer,
                    active = true
                ),
                ProductEntity(
                    storeId = store.storeId,
                    name = "Hamburguer Smash Truck",
                    description = "Pão, carne, queijo",
                    productCode = "HT02",
                    priceCents = 1000,
                    imageUrl = R.drawable.frango,
                    active = true
                )
            )
        )

        val addonIds = addonDAO.insertAddons(
            listOf(

                AddonEntity(
                    storeId = store.storeId,
                    name = "Ketchup",
                    priceCents = 190
                ),
                AddonEntity(
                    storeId = store.storeId,
                    name = "Carne",
                    priceCents = 490
                ),
                AddonEntity(
                    storeId = store.storeId,
                    name = "cebola",
                    priceCents = 190
                ),

            )
        )


        val relations = productIds.flatMap { productId ->
            addonIds.map { addonId ->
                ProductAddonEntity(
                    productId = productId.toInt(),
                    addonId = addonId.toInt()
                )
            }
        }

        productAddonDAO.insertProductAddons(relations)
        Log.d("SEED", "Store - " + store.storeId.toString() + "account -")


    }
}
