package com.jesse.sickstech.features.menu


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import coil.load
import com.jesse.sickstech.R
import com.jesse.sickstech.databinding.ItemMenuBinding

//adapter tradicional , menos performatico
/*
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
        holder.binding.imageProduct.load(R.drawable.hamburguer){
            crossfade(true)
            placeholder(R.drawable.hamburguer)
            error(R.drawable.hamburguer)
        }
        holder.binding.textViewTitle.text = menu.titulo
        holder.binding.textViewPreco.text = menu.preco
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

}

 */


class MenuDiffCallback : DiffUtil.ItemCallback<Menu>() {
    override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean = oldItem.titulo == newItem.titulo
    override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean = oldItem == newItem
}

class MenuAdapter : ListAdapter<Menu, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

    inner class MenuViewHolder(val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu) {
            binding.textViewTitle.text = menu.titulo
            binding.textViewPreco.text = menu.preco

            binding.imageProduct.load(menu.imagem) {
                crossfade(true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMenuBinding.inflate(inflater, parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}