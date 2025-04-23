package com.example.nukkadeats.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nukkadeats.databinding.CartItemsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class cartAdapter(
    private val context : Context,
    val cartItem: MutableList<String>,
    val cartPrice: MutableList<String>,
    val cartImage: MutableList<String>,
    private val cartDescription: MutableList<String>,
    private val cartQuantity: MutableList<Int>

) : RecyclerView.Adapter<cartAdapter.cartViewHolder>() {

    //Initialize Firebase Auth
    private val auth = FirebaseAuth.getInstance()

    init {
        //initialize Firebase
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = cartItem.size

        itemQuantity = IntArray(cartItem.size) { 1 }
        cartItemsReference = database.reference.child("users").child(userId).child("cartItems")


    }

    companion object {
        private var itemQuantity: IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference

    }


    inner class cartViewHolder(private val binding: CartItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantity[position]

                nameCartItem.text = cartItem[position]
                cartItemPrice.text = cartPrice[position]

                val uriString = cartImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(foodImage)

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