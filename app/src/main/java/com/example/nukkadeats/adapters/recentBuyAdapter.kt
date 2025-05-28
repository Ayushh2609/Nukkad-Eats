package com.example.nukkadeats.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nukkadeats.databinding.RecentBuyItemsBinding

class recentBuyAdapter (
    private var context : Context,
    private val foodNameList : ArrayList<String>,
    private val foodImageList : ArrayList<String>,
    private val foodPriceList : ArrayList<String>,
    private val foodQuantityList : ArrayList<Int>,
    ) :RecyclerView.Adapter<recentBuyAdapter.RecentViewHolder>() {
    inner class RecentViewHolder (private val binding: RecentBuyItemsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {

            binding.apply {
                menuItemName.text = foodNameList[position]
                menuItemPrice.text = foodPriceList[position]
                menuItemDescription.text = foodQuantityList[position].toString()

                val uriString = foodImageList[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(menuItemImage)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding = RecentBuyItemsBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return RecentViewHolder(binding)
    }

    override fun getItemCount(): Int = foodNameList.size

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(position)
    }
}