package com.example.nukkadeats.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeats.ItemDetailsActivity
import com.example.nukkadeats.Modal.MenuItemModal
import com.example.nukkadeats.databinding.MenuItemsBinding

class MenuAdapter(private val menuItems : List<MenuItemModal>,
                  private var context : Context) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemClickListener : OnClickListener ?= null

    inner class MenuViewHolder(private val binding : MenuItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        init{
            binding.root.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    openDetailsActivity(position)
                }
            }
        }

        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]

            //An intent to open detail activity and pass data.
            val intent = Intent(context , ItemDetailsActivity::class.java).apply {
                putExtra("foodName" , menuItem.foodName)
                putExtra("foodPrice" , menuItem.foodPrice)
                putExtra("foodImageUrl" , menuItem.foodImageUrl)
                putExtra("foodDescription" , menuItem.foodDescription)
                putExtra("foodIngredients" , menuItem.foodIngredients)
            }

            //Start the ItemDetailed Activty
            context.startActivity(intent)

        }

        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menuItemName.text = menuItem.foodName
                menuItemDescription.text = menuItem.foodDescription
                menuItemPrice.text = menuItem.foodPrice

                val uri = Uri.parse(menuItem.foodImageUrl)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemsBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    interface OnClickListener{
        fun onItemClick(position: Int)

    }
}


