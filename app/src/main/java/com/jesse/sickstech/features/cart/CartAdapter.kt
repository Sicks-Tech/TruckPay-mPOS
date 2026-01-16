package com.jesse.sickstech.features.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.jesse.sickstech.databinding.ItemCartBinding

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

    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {
        val cart = cartItemList[position]
        holder.binding.textViewNumberItem.text = cart.id.toString()
        holder.binding.textViewProdutoCodigo.text = cart.codigo
        holder.binding.textViewProdutoNome.text = cart.nome
        holder.binding.textViewPrecoUnitario.text = "R$ ${ cart.precoUnitario}"
        holder.binding.textViewPrecoTotal.text = "R$${ cart.precoUnitario}"

    }

    override fun getItemCount(): Int {
      return cartItemList.size
    }




}