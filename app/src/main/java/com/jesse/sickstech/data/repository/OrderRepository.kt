package com.jesse.sickstech.data.repository

import android.content.Context
import androidx.room.withTransaction
import com.jesse.sickstech.data.AppDataBase
import com.jesse.sickstech.data.local.entity.CartItemAddonsEntity
import com.jesse.sickstech.data.local.entity.CartItemEntity
import com.jesse.sickstech.domain.model.Addon
import com.jesse.sickstech.domain.model.AddonsState
import com.jesse.sickstech.domain.model.CartItem
import com.jesse.sickstech.domain.model.CartItemWithProduct
import com.jesse.sickstech.domain.model.SelectedAddon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/*
deve lhe dar com order/orderItem/payment/transaction
 */
class OrderRepository private constructor(context: Context) {
    private val database = AppDataBase.getInstance(context.applicationContext)


    private val cartItem = database.cartItemDAO()
    private val cartItemAddons = database.cartItemAddonsDAO()


    companion object {
        @Volatile
        private var instance: OrderRepository? = null

        fun getInstance(context: Context): OrderRepository {
            return instance ?: synchronized(this) {
                instance ?: OrderRepository(context).also { instance = it }
            }
        }
    }

    suspend fun addItemToCart(accountId: Int, productId: Int, quantity: Int) {
        val entity = CartItemEntity(
            accountId = accountId,
            productId = productId,
            quantity = quantity
        )

        // Tenta inserir. Se retornar -1, significa que o item já existe (conflito de conta/produto)
        val id = cartItem.insert(entity)
        if (id == -1L) {
            cartItem.updateQuantity(accountId, productId, quantity)
        }
    }


    suspend fun removeItem(accountId: Int, productId: Int) {
        cartItem.removeProduct(accountId, productId)
    }

    suspend fun clearCart(accountId: Int) {
        cartItem.clearCart(accountId)
    }



//    fun getCartItems(accountId: Int): Flow<List<CartItem>> {
//        // 1. Chamamos o DAO que retorna a relação (CartItem + Product)
//        return cartItem.getCartWithProduct(accountId).map { list ->
//            // 2. Mapeamos cada item da lista para o modelo de Domínio
//            list.map { item ->
//                CartItem(
//                    id = item.cartItem.cartItemId,
//                    productId = item.cartItem.productId,
//                    productName = item.product.name, // Vem da Relation
//                    productCode = item.product.productCode, // Vem da Relation
//                    productPrice = item.product.priceCents.toBigDecimal().movePointLeft(2),
//                    quantity = item.cartItem.quantity,
//                    createdAt = item.cartItem.createdAt
//                )
//            }
//        }
//    }


    fun getCartItems(accountId: Int): Flow<List<CartItem>> {
        return cartItem.getCartFullDetails(accountId).map { list ->
            list.map { full ->
                CartItem(
                    id = full.cartItemWithProduct.cartItem.cartItemId,
                    productId = full.cartItemWithProduct.cartItem.productId,
                    productName = full.cartItemWithProduct.product.name,
                    productCode = full.cartItemWithProduct.product.productCode,
                    productPrice = full.cartItemWithProduct.product.priceCents.toBigDecimal().movePointLeft(2),
                    quantity = full.cartItemWithProduct.cartItem.quantity,
                    // Mapeando os addons que vieram do banco
                    selectedAddons = full.addons.map { a ->
                        SelectedAddon(
                            addon = Addon(
                                id = a.addonDetails.addonId,
                                name = a.addonDetails.name,
                                price = a.addonDetails.priceCents.toBigDecimal().movePointLeft(2)
                            ),
                            quantity = a.relation.quantity
                        )
                    },
                    createdAt = full.cartItemWithProduct.cartItem.createdAt
                )
            }
        }
    }

    // No OrderRepository
    suspend fun saveFullCartItem(accountId: Int, state: AddonsState) {
        // Usamos o withTransaction para garantir que se um falhar, nada é salvo (Atomicidade)
        database.withTransaction {
            // 1. Criar e inserir o Item do Carrinho (ex: Hambúrguer)
            val cartItemEntity = CartItemEntity(
                accountId = accountId,
                productId = state.productId,
                quantity = 1, // Quantidade do item principal
                createdAt = System.currentTimeMillis()
            )

            // O insert deve retornar o ID (Long) gerado pelo Room
            val cartItemId = database.cartItemDAO().insert(cartItemEntity)

            // Se o insert falhou ou retornou erro (caso tenhas índices únicos), podes tratar aqui
            if (cartItemId != -1L) {

                // 2. Filtrar apenas os adicionais que o utilizador realmente selecionou (qtd > 0)
                val selectedAddons = state.addons.filter { it.quantity > 0 }

                if (selectedAddons.isNotEmpty()) {
                    // 3. Mapear os SelectedAddon para a Entidade do Banco
                    val addonsToSave = selectedAddons.map { selected ->
                        CartItemAddonsEntity(
                            cartItemId = cartItemId, // ID que acabámos de criar acima
                            addonId = selected.addon.id,
                            quantity = selected.quantity
                        )
                    }

                    // 4. Salvar todos os adicionais de uma vez
                    database.cartItemAddonsDAO().insertAll(addonsToSave)
                }
            }
        }
    }
}

// Exemplo de como o Mapper ficaria no OrderRepository
private fun mapToDomain(
    itemDetailed: CartItemWithProduct,
    selectedAddons: List<SelectedAddon>
): CartItem {
    return CartItem(
        id = itemDetailed.cartItem.cartItemId,
        productId = itemDetailed.cartItem.productId,
        productName = itemDetailed.product.name,
        productCode = itemDetailed.product.productCode,
        productPrice = itemDetailed.product.priceCents.toBigDecimal().movePointLeft(2),
        quantity = itemDetailed.cartItem.quantity,
        selectedAddons = selectedAddons,
        createdAt = itemDetailed.cartItem.createdAt
    )
}