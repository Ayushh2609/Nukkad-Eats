package com.example.nukkadeats

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.nukkadeats.Modal.CartItems
import com.example.nukkadeats.databinding.ActivityItemDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ItemDetailsActivity : AppCompatActivity() {
    private var foodName: String? = null
    private var foodDescription: String? = null
    private var foodIngredients: String? = null
    private var foodImage: String? = null
    private var foodPrice: String? = null

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityItemDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemDetailsBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Initializing Auth
        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("foodName")
        foodDescription = intent.getStringExtra("foodDescription")
        foodIngredients = intent.getStringExtra("foodIngredient")
        foodImage = intent.getStringExtra("foodImageUrl")

        foodPrice = intent.getStringExtra("foodPrice")

        binding.foodName.text = foodName
        binding.foodDescription.text = foodDescription
        binding.foodIngredients.text = foodIngredients
        Glide.with(this@ItemDetailsActivity).load(Uri.parse(foodImage))
            .into(binding.foodDescriptionImage)

//        binding.foodName.text = foodPrice

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addToCartBtn.setOnClickListener {
            addItemToCart()

        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""

        //Create a cart Item Object
        val cartItems = CartItems(
            foodName.toString(),
            foodPrice.toString(),
            foodImage.toString(),
            foodDescription.toString(),
            1
        )

        database.child("users").child(userId).child("cartItems").push().setValue(cartItems)
            .addOnSuccessListener {
                Toast.makeText(this, "Items added to cart successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Items added to cart Failed", Toast.LENGTH_SHORT).show()
                finish()
            }


    }
}