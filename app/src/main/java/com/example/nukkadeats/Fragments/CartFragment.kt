package com.example.nukkadeats.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.CartProceed
import com.example.nukkadeats.Modal.CartItems
import com.example.nukkadeats.adapters.cartAdapter
import com.example.nukkadeats.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodImageUrl: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: cartAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        //Initializing Auth
        auth = FirebaseAuth.getInstance()

        retrieveCartItems()


        binding.proceedBtn.setOnClickListener {
            val intent = Intent(requireContext(), CartProceed::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun retrieveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""

        val foodReference: DatabaseReference =
            database.reference.child("users").child(userId).child("cartItems")

        //List to store cart items
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodImageUrl = mutableListOf()
        foodDescriptions = mutableListOf()
        foodIngredients = mutableListOf()
        quantity = mutableListOf()

        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {

                for (foodSnapshot in snapshot.children) {

                    //Get the cart items object from the Child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodImage?.let { foodImageUrl.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredients.add(it) }

                }

                setAdapter()
            }

            private fun setAdapter() {
                val adapter = cartAdapter(
                    requireContext(),
                    foodNames,
                    foodPrices,
                    foodImageUrl,
                    foodDescriptions,
                    foodIngredients,
                    quantity
                )

                binding.cartRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter = adapter

                if (adapter.itemCount == 0) {
                    binding.cardTotalAmount.visibility = View.GONE
                } else {
                    binding.cardTotalAmount.visibility = View.VISIBLE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Data Not Fetched", Toast.LENGTH_SHORT).show()
            }
        })
    }

}