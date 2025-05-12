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
import com.example.nukkadeats.adapters.OnQuantityChangeListener
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

    private lateinit var totalAmoutPrice: String
    private lateinit var finalAmountPrice: String
    private val deliveryCharges = 10
    private val discount = 10


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
            //Getting order items details before going to payment Activity
            getOrderItemsDetails()
        }

        return binding.root
    }

    private fun calculateTotalAmount(
        Price: MutableList<String>,
        quantities: MutableList<Int>
    ): Int {

        var totalAmount = 0
        for (i in 0 until Price.size) {
            var price = Price[i]
            val lastChar = price.last()
            val priceIntVal = if (lastChar == '$') {
                price.dropLast(1).toInt()
            } else {
                price.toInt()

            }
            var quantity = quantities[i]
            totalAmount += priceIntVal * quantity
        }

        return totalAmount
    }

    private fun getOrderItemsDetails() {

        val orderIdReference: DatabaseReference =
            database.reference.child("users").child(userId).child("cartItems")

        val Name = mutableListOf<String>()
        val Price = mutableListOf<String>()
        val ImageUri = mutableListOf<String>()
        val Description = mutableListOf<String>()
        val Ingredient = mutableListOf<String>()


        //Get tem Quantities
        val quantities = cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {

                    //Get the cart items object from the Child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    cartItems?.foodName?.let { Name.add(it) }
                    cartItems?.foodPrice?.let { Price.add(it) }
                    cartItems?.foodDescription?.let { Description.add(it) }
                    cartItems?.foodImage?.let { ImageUri.add(it) }
                    cartItems?.foodIngredient?.let { Ingredient.add(it) }


                }
                orderNow(Name, Price, ImageUri, Description, Ingredient, quantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Data Not Fetched", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun orderNow(
        name: MutableList<String>,
        price: MutableList<String>,
        imageUri: MutableList<String>,
        description: MutableList<String>,
        ingredient: MutableList<String>,
        quantities: MutableList<Int>
    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), CartProceed::class.java)

            intent.putExtra("foodItemName", name as ArrayList<String>)
            intent.putExtra("price", price as ArrayList<String>)
            intent.putExtra("imgUri", imageUri as ArrayList<String>)
            intent.putExtra("description", description as ArrayList<String>)
            intent.putExtra("ingredients", ingredient as ArrayList<String>)
            intent.putExtra("quantity", quantities as ArrayList<Int>)

            startActivity(intent)
        }
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

                totalAmoutPrice = calculateTotalAmount(foodPrices, quantity).toString()

                //Setting Subtotal Amount on the CardView
                binding.subTotalAmount.setText(totalAmoutPrice)


                //Setting Delivery Charge Amount on the CardView
                binding.deliveryChargeAmount.setText(deliveryCharges.toString())
                binding.discountAmount.setText(discount.toString())


                //Setting Total Amount on the CardView
                finalAmountPrice =
                    ((totalAmoutPrice.toInt() - ((totalAmoutPrice.toInt() * 10) / 100)) + deliveryCharges).toString()
                binding.totalAmount.setText(finalAmountPrice)

                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = cartAdapter(
                    requireContext(),
                    foodNames,
                    foodPrices,
                    foodImageUrl,
                    foodDescriptions,
                    foodIngredients,
                    quantity,
                    object : OnQuantityChangeListener {
                        override fun onQuantityChanged() {
                            updateAmountViews()
                        }
                    }
                )

                binding.cartRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                //Payment Cart Visible or not
                if (cartAdapter.itemCount == 0) {
                    binding.cardTotalAmount.visibility = View.GONE
                } else {
                    binding.cardTotalAmount.visibility = View.VISIBLE
                }

                binding.cartRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Data Not Fetched", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateAmountViews() {
        val quant = cartAdapter.getUpdatedItemsQuantities()
        val totalAmt = calculateTotalAmount(foodPrices, quant)

        totalAmoutPrice = totalAmt.toString()
        binding.subTotalAmount.setText(totalAmoutPrice)

        finalAmountPrice = ((totalAmt - ((totalAmt * discount) / 100)) + deliveryCharges).toString()
        binding.totalAmount.setText(finalAmountPrice)
    }
}
