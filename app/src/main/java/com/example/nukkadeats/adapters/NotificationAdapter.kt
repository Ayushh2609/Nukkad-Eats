package com.example.nukkadeats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeats.databinding.NotificationItemBinding

class NotificationAdapter(private var notiMessage : ArrayList<String> , private var notiImage : ArrayList<Int>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): NotificationAdapter.NotificationViewHolder {

        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NotificationAdapter.NotificationViewHolder,
        position: Int
    ) {

        holder.bind(position)
    }

    override fun getItemCount(): Int = notiMessage.size


    inner class NotificationViewHolder(private val binding : NotificationItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                notificationTextView.text = notiMessage[position]
                notificationImage.setImageResource(notiImage[position])


            }
        }

    }
}
