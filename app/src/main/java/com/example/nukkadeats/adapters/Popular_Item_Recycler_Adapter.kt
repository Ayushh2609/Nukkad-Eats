package com.example.nukkadeats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.example.nukkadeats.databinding.PopularItemRecyclerBinding

class Popular_Item_Recycler_Adapter(private val itemsName : List<String>, private val itemPrice : List<String> ,private val itemImage : List<Int> , private val itemDescription : List<String>) : RecyclerView.Adapter<Popular_Item_Recycler_Adapter.ViewHolder>() {
    class ViewHolder (private val binding: PopularItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imgView =  binding.popularFoodImage
        fun bind(nameItem: String, img: Int, priceItem: String , descriptionItem: String){
            binding.popularFoodName.text = nameItem
            binding.popularFoodPrice.text = priceItem
            binding.popularFoodDescription.text = descriptionItem
            imgView.setImageResource(img)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PopularItemRecyclerBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun getItemCount(): Int {
        return itemsName.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val namesItem = itemsName[position]
        val imageItem = itemImage[position]
        val priceItem = itemPrice[position]
        val descriptionItem = itemDescription[position]

        holder.bind(namesItem, imageItem, priceItem , descriptionItem)
    }
}