package com.jesse.sickstech.data.repository

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.jesse.sickstech.data.AppDataBase
import com.jesse.sickstech.data.api.RetrofitHelper.api
import com.jesse.sickstech.data.local.entity.CartItemAddonsEntity
import com.jesse.sickstech.data.local.entity.CartItemEntity
import com.jesse.sickstech.data.local.entity.OrderEntity
import com.jesse.sickstech.data.local.entity.OrderItemAddonEntity
import com.jesse.sickstech.data.local.entity.OrderItemEntity
import com.jesse.sickstech.domain.enums.OrderStatus
import com.jesse.sickstech.domain.enums.SyncStatus
import com.jesse.sickstech.domain.mapper.toOrderStatus
import com.jesse.sickstech.domain.model.Addon
import com.jesse.sickstech.domain.model.AddonsState
import com.jesse.sickstech.domain.model.CartItem
import com.jesse.sickstech.domain.model.CreateOrder
import com.jesse.sickstech.domain.model.SelectedAddon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal

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


    fun getCartTotal(accountId: Int): Flow<BigDecimal> {
        return getCartItems(accountId).map { items ->
            items.sumOf { item ->
                val productTotal = item.productPrice.multiply(item.quantity.toBigDecimal())
                val addonsTotal = item.selectedAddons.sumOf { addon ->
                    addon.addon.price.multiply(addon.quantity.toBigDecimal())
                }
                productTotal.add(addonsTotal)
            }
        }
    }


    suspend fun createOrderFromCart(
        accountId: Int,
        storeId: Int
    ): CreateOrder {

        return database.withTransaction {

            val cartItemsList = cartItem.getCartFullDetailsList(accountId)

            if (cartItemsList.isEmpty()) {
                throw IllegalStateException("Carrinho vazio")
            }

            val total = cartItemsList.sumOf { full ->

                val productTotal =
                    full.cartItemWithProduct.product.priceCents
                        .toBigDecimal()
                        .movePointLeft(2)
                        .multiply(
                            full.cartItemWithProduct.cartItem.quantity.toBigDecimal()
                        )

                val addonsTotal = full.addons.sumOf { a ->
                    a.addonDetails.priceCents
                        .toBigDecimal()
                        .movePointLeft(2)
                        .multiply(a.relation.quantity.toBigDecimal())
                }

                productTotal.add(addonsTotal)
            }

            val orderEntity = OrderEntity(
                storeId = storeId,
                accountId = accountId,
                totalCents = total.multiply(BigDecimal(100)).toInt(),
                status = OrderStatus.OPEN,
                createdAt = System.currentTimeMillis()
            )

            val orderId = database.orderDAO().insert(orderEntity).toInt()

            Log.d("OrderRepo", "Processando $orderId: ${cartItemsList.size} itens")

            cartItemsList.forEach { full ->

                val orderItem = OrderItemEntity(
                    orderId = orderId,
                    productId = full.cartItemWithProduct.cartItem.productId,
                    quantity = full.cartItemWithProduct.cartItem.quantity,
                    unitPriceCents = full.cartItemWithProduct.product.priceCents
                )

                val orderItemId =
                    database.orderItemDAO().insertOrderItem(orderItem).toInt()

                full.addons.forEach { a ->

                    val addonEntity = OrderItemAddonEntity(
                        orderItemId = orderItemId,
                        addonId = a.addonDetails.addonId,
                        quantity = a.relation.quantity,
                        priceDeltaCents = a.addonDetails.priceCents
                    )

                    database.orderItemAddonDAO().insertAddonToItem(addonEntity)
                }
            }

            cartItem.clearCart(accountId)

            CreateOrder(
                orderId = orderId,
                total = total
            )
        }
    }


    suspend fun saveFullCartItem(accountId: Int, state: AddonsState) {
        // Usamos o withTransaction para garantir que se um falhar, nada é salvo (Atomicidade)
        database.withTransaction {
            //  Criar e inserir o Item do Carrinho
            val cartItemEntity = CartItemEntity(
                accountId = accountId,
                productId = state.productId,
                quantity = 1, // Quantidade do item principal
                createdAt = System.currentTimeMillis()
            )

            val cartItemId = database.cartItemDAO().insert(cartItemEntity)

            if (cartItemId != -1L) {


                val selectedAddons = state.addons.filter { it.quantity > 0 }

                if (selectedAddons.isNotEmpty()) {
                    //  Mapear os SelectedAddon para a Entidade do Banco
                    val addonsToSave = selectedAddons.map { selected ->
                        CartItemAddonsEntity(
                            cartItemId = cartItemId,
                            addonId = selected.addon.id,
                            quantity = selected.quantity
                        )
                    }

                    //  Salvar todos os adicionais de uma vez
                    database.cartItemAddonsDAO().insertAll(addonsToSave)
                }
            }
        }
    }
}