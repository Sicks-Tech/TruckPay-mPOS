package com.jesse.sickstech.features.menu.addons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jesse.sickstech.databinding.AddonsItemBinding
import com.jesse.sickstech.domain.model.Addon
import com.jesse.sickstech.domain.model.SelectedAddon


class AddonsDiffCallback : DiffUtil.ItemCallback<SelectedAddon>() {
    override fun areItemsTheSame(
        oldItem: SelectedAddon,
        newItem: SelectedAddon
    ): Boolean {
        return oldItem.addon.id == newItem.addon.id
    }

    override fun areContentsTheSame(
        oldItem: SelectedAddon,
        newItem: SelectedAddon
    ): Boolean {
        return oldItem == newItem
    }
}


class AddonsAdapter (
    private val onAdd: (SelectedAddon) -> Unit,
    private val onSubtract: (SelectedAddon) -> Unit
) : ListAdapter<SelectedAddon, AddonsAdapter.AddonsviewHolder>(AddonsDiffCallback()){


    inner class AddonsviewHolder(val binding: AddonsItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: SelectedAddon) {
            binding.textViewName.text = item.addon.name
            binding.btnQuantity.text = item.quantity.toString()
            binding.textViewUnityPrice.text ="R$ ${item.addon.price}"



            binding.btnAdicionar.setOnClickListener {
                onAdd(item)
            }

            binding.btnSubtract.setOnClickListener {
                onSubtract(item)
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddonsviewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AddonsItemBinding.inflate(inflater, parent, false)
        return AddonsviewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddonsviewHolder,aposition: Int) {
        holder.bind(getItem(aposition))
    }


}