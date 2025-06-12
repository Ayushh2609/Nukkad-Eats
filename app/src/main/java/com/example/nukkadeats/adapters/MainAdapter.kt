package com.mindorks.example.shimmer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nukkadeats.Modal.User
import com.example.nukkadeats.R

class MainAdapter(
    private val users: ArrayList<User>
) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameCartItem)
        private val emailTextView: TextView = itemView.findViewById(R.id.cartItemPrice)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.foodImage)

        fun bind(user: User) {
            nameTextView.text = user.name
            emailTextView.text = user.email
            Glide.with(avatarImageView.context)
                .load(user.avatar)
                .into(avatarImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
                false
            )
        )


    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(users[position])

    fun addData(list: List<User>) {
        users.addAll(list)
    }

}