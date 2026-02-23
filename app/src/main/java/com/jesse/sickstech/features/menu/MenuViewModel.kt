package com.jesse.sickstech.features.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesse.sickstech.data.local.entity.CartItemEntity
import com.jesse.sickstech.data.repository.OrderRepository
import com.jesse.sickstech.data.repository.ShopRepository
import com.jesse.sickstech.domain.model.Addon
import com.jesse.sickstech.domain.model.AddonsState
import com.jesse.sickstech.domain.model.Menu
import com.jesse.sickstech.domain.model.SelectedAddon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MenuViewModel(
    private val repository: ShopRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _menuItems = MutableLiveData<List<Menu>>()
    val menuItems: LiveData<List<Menu>> = _menuItems

//    private val _addons = MutableLiveData<List<Addon>>()
//    val addons: LiveData<List<Addon>> = _addons

    private val _state = MutableStateFlow(AddonsState())
    val state: StateFlow<AddonsState> = _state


    fun loadProducts(storeId: Int) {
        viewModelScope.launch {
            val products = repository.getProducts(storeId)

            val uiItems = products
                .filter { it.active }
                .map { product ->
                    Menu(
                        id = product.productId,
                        imagem = product.imageUrl,
                        titulo = product.name,
                        precoCents = product.priceCents
                            .toBigDecimal()
                            .movePointLeft(2)
                    )
                }

            _menuItems.value = uiItems
        }
    }

//    fun loadAddons(productId: Int) {
//        viewModelScope.launch {
//            repository.getAddons(productId).collect { addons ->
//                _addons.value = addons
//            }
//        }
//    }

    fun loadAddons(productId: Int, precoBase: BigDecimal) {
        viewModelScope.launch {
            repository.getAddons(productId).collect { addons ->

                val selected = addons.map {
                    SelectedAddon(
                        addon = it,
                        quantity = 0
                    )
                }

                _state.value = AddonsState(
                    productId = productId,
                    precoBase = precoBase,
                    addons = selected,
                    total = precoBase
                )
            }
        }
    }



    fun incrementAddon(addonId: Int) {
        updateAddonQuantityByDelta(addonId, +1)
    }

    fun decrementAddon(addonId: Int) {
        updateAddonQuantityByDelta(addonId, -1)
    }

    private fun updateAddonQuantityByDelta(addonId: Int, delta: Int) {

        val currentState = _state.value

        val updatedAddons = currentState.addons.map { selected ->
            if (selected.addon.id == addonId) {
                selected.copy(
                    quantity = (selected.quantity + delta).coerceAtLeast(0)
                )
            } else {
                selected
            }
        }

        val addonsTotal = calculateAddonsTotal(updatedAddons)

        _state.value = currentState.copy(
            addons = updatedAddons,
            addonsTotal = addonsTotal,
            total = calculateTotal(currentState.precoBase, updatedAddons)

        )
    }


    private fun calculateTotal(
        precoBase: BigDecimal,
        addons: List<SelectedAddon>
    ): BigDecimal {

        val addonsTotal = addons.fold(BigDecimal.ZERO) { acc, selected ->
            acc + (selected.addon.price * BigDecimal(selected.quantity))
        }

        return precoBase + addonsTotal
    }

    private fun calculateAddonsTotal(
        addons: List<SelectedAddon>
    ): BigDecimal {

        return addons.fold(BigDecimal.ZERO) { acc, selected ->
            acc + (selected.addon.price * BigDecimal(selected.quantity))
        }
    }


    fun addItemToCart(accountId: Int, state: AddonsState) {
        viewModelScope.launch {
            try {
//                orderRepository.addItemToCart(accountId, productId, quantity)
            orderRepository.saveFullCartItem(accountId, state)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("MenuViewModel", "Erro ao adicionar item ao carrinho: ${e.message}")
            }
        }
    }



}