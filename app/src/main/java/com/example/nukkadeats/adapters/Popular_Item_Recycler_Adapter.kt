package com.example.nukkadeats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeats.databinding.PopularItemRecyclerBinding

class Popular_Item_Recycler_Adapter : RecyclerView.Adapter<Popular_Item_Recycler_Adapter.ViewHolder>() {
    class ViewHolder (private val binding: PopularItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PopularItemRecyclerBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}