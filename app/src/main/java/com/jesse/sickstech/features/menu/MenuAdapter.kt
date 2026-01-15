package com.jesse.sickstech.features.menu


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.jesse.sickstech.databinding.ItemMenuBinding

class MenuAdapter : Adapter<MenuAdapter.MenuViewHolder>() {
    private var menuList = listOf<Menu>()

    fun atualizarListaDados(list :List<Menu>){
        menuList = list
        notifyDataSetChanged() // não performatico , mas aceitavel , pra listar pequenas , que por hora é mock
    }

    inner class MenuViewHolder(val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {}


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)

        val itemView = ItemMenuBinding.inflate(
            layoutinflater,
            parent,
            false
        )

        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MenuViewHolder,
        position: Int
    ) {
        val menu = menuList[position]
        holder.binding.imageProduct.setImageResource(menu.imagem)
        holder.binding.textViewTitle.text = menu.titulo
        holder.binding.textViewPreco.text = menu.preco
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

}