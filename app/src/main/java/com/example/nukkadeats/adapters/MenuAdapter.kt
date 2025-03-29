package com.example.nukkadeats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeats.databinding.MenuItemsBinding

class MenuAdapter(private val menuName : MutableList<String> , private val menuDescription : MutableList<String> , private val menuPrice : MutableList<String> , private val menuImg : MutableList<Int>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    inner class MenuViewHolder(private val binding : MenuItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                menuItemName.text = menuName[position]
                menuItemDescription.text = menuDescription[position]
                menuItemPrice.text = menuPrice[position]
                menuItemImage.setImageResource(menuImg[position])

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemsBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuName.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }
}