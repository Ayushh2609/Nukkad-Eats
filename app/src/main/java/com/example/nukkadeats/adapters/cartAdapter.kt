package com.example.nukkadeats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nukkadeats.databinding.CartItemsBinding

class cartAdapter(
    val cartItem: MutableList<String>,
    val cartPrice: MutableList<String>,
    val cartImage: MutableList<Int>
) : RecyclerView.Adapter<cartAdapter.cartViewHolder>() {
    private val itemQuantity = IntArray(cartItem.size) { 1 }

    inner class cartViewHolder(private val binding: CartItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]

                nameCartItem.text = cartItem[position]
                descriptionCartItem.text = cartPrice[position]
                foodImage.setImageResource(cartImage[position])
                quantityCart.text = quantity.toString()

                minusCart.setOnClickListener {
                    decreaseQuantity(position)

                }
                plusCart.setOnClickListener {
                    increaseQuantity(position)
                }

                deleteCart.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteQuantity(position)

                    }
                }
            }

        }

        private fun deleteQuantity(position: Int) {
            cartItem.removeAt(position)
            cartPrice.removeAt(position)
            cartImage.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItem.size)
        }

        fun increaseQuantity(position: Int) {
            if (itemQuantity[position] < 20) {
                itemQuantity[position]++
                binding.quantityCart.text = itemQuantity[position].toString()
            }
        }

        fun decreaseQuantity(position: Int) {
            if (itemQuantity[position] > 1) {
                itemQuantity[position]--
                binding.quantityCart.text = itemQuantity[position].toString()
            }
            if (itemQuantity[position] == 1) {
                val itemPosition = adapterPosition
                if (itemPosition != RecyclerView.NO_POSITION) {
                    deleteQuantity(position)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val binding = CartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cartViewHolder(binding)
    }

    override fun getItemCount(): Int = cartItem.size

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        holder.bind(position)
    }
}