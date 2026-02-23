package com.jesse.sickstech.features.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.jesse.sickstech.databinding.ItemCartBinding
import com.jesse.sickstech.domain.model.CartItem

class CartAdapter : Adapter<CartAdapter.CartViewHolder>() {
    private var cartItemList = listOf<CartItem>()

    fun atualizarListaDados(list: List<CartItem>){
        cartItemList = list
        notifyDataSetChanged()
    }


    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {}


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)

        val itemview = ItemCartBinding.inflate(
            layoutinflater,
            parent,
            false
        )

        return CartViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = cartItemList[position]

        with(holder.binding) {
            // Exibe a posição ou o ID do item
            textViewNumberItem.text = (position + 1).toString()

            textViewProdutoCodigo.text = cart.productCode
            textViewProdutoNome.text = cart.productName

            textViewPrecoUnitario.text = "R$ ${cart.productPrice}"


            if (cart.selectedAddons.isNotEmpty()) {
                val totalAddons = cart.selectedAddons.sumOf {
                    it.addon.price.multiply(it.quantity.toBigDecimal())
                }
                textViewPrecoAdd.text = "+R$$totalAddons"
            } else {
                textViewPrecoAdd.text = "R$0,00"
            }

            Log.d("CartAdapter", "Itens no carrinho: ${cart.selectedAddons}")

            val totalItem = "${cart.totalWithAddons}"
            textViewPrecoTotal.text = "R$ $totalItem"

        }
    }
    override fun getItemCount(): Int {
      return cartItemList.size
    }




}