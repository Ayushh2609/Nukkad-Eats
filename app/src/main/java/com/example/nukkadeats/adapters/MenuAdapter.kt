package com.example.nukkadeats.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeats.ItemDetailsActivity
import com.example.nukkadeats.databinding.MenuItemsBinding

class MenuAdapter(private val menuName : MutableList<String> , private val menuDescription : MutableList<String> , private val menuPrice : MutableList<String> , private val menuImg : MutableList<Int> , private var context : Context) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemClickListener : OnClickListener ?= null

    inner class MenuViewHolder(private val binding : MenuItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        init{
            binding.root.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    itemClickListener?.onItemClick(position)
                }

                //setOnClickListener to open details

                val intent = Intent(context , ItemDetailsActivity::class.java)
                intent.putExtra("MenuItemName" , menuName.get(position))
                intent.putExtra("MenuItemImage" , menuImg.get(position))

                context.startActivity(intent)
            }
        }

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

    interface OnClickListener{
        fun onItemClick(position: Int)

    }
}


