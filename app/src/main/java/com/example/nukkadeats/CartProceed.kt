package com.example.nukkadeats

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeats.Modal.OrderDetaild
import com.example.nukkadeats.adapters.cartAdapter
import com.example.nukkadeats.databinding.ActivityCartProceedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartProceed : AppCompatActivity() {
    private lateinit var binding: ActivityCartProceedBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String

    private lateinit var foodItemName: ArrayList<String>
    private lateinit var price: ArrayList<String>
    private lateinit var imgUri: ArrayList<String>
    private lateinit var description: ArrayList<String>
    private lateinit var ingredients: ArrayList<String>
    private lateinit var quantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartProceedBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference()
        userId = auth.currentUser?.uid ?: ""

        setUserData()

        val intent = intent
        foodItemName = intent.getStringArrayListExtra("foodItemName") as ArrayList<String>
        price = intent.getStringArrayListExtra("price") as ArrayList<String>
        imgUri = intent.getStringArrayListExtra("imgUri") as ArrayList<String>
        description = intent.getStringArrayListExtra("description") as ArrayList<String>
        ingredients = intent.getStringArrayListExtra("ingredients") as ArrayList<String>
        quantities = intent.getIntegerArrayListExtra("quantity") as ArrayList<Int>


        //Total Price Calculation
        totalAmount = calculateTotalAmount().toString()

        binding.passwordEditText.setText(totalAmount)


        //Place My Order
        binding.placeMyOrder.setOnClickListener {

            name = binding.nameEditText.text.toString().trim()
            address = binding.addressEditText.text.toString().trim()
            phone = binding.phoneEditText.text.toString().trim()

            if (name.isBlank() && address.isBlank() && phone.isBlank()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }

            val BottomSheetDialog = OrderPlaced()
            BottomSheetDialog.show(supportFragmentManager, "Test")
        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetaild = OrderDetaild(
            userId,
            name,
            foodItemName,
            imgUri,
            price,
            quantities,
            address,
            totalAmount,
            phone,
            false,
            false,
            itemPushKey,
            time
        )

        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetaild).addOnSuccessListener {
            val BottomSheetDialog = OrderPlaced()
            BottomSheetDialog.show(supportFragmentManager, "Test")

            removeItemFromCart()
            addOrderToHistory(orderDetaild)
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed To order", Toast.LENGTH_SHORT).show()
            }

    }

    private fun addOrderToHistory(orderDetaild: OrderDetaild) {
        databaseReference.child("users").child(userId).child("OrderHistory")
            .child(orderDetaild.itemPushKey!!)
            .setValue(orderDetaild).addOnSuccessListener {
            }
    }

    private fun removeItemFromCart() {
        val cartItemReference = databaseReference.child("users").child(userId).child("cartItems")
        cartItemReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {

        var totalAmount = 0
        for (i in 0 until price.size) {
            var price = price[i]
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

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userReference = databaseReference.child("users").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        val names = snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresses = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phoneNumbers =
                            snapshot.child("phone").getValue(String::class.java) ?: ""

                        binding.apply {
                            nameEditText.setText(names)
                            addressEditText.setText(addresses)
                            phoneEditText.setText(phoneNumbers)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

    }
}