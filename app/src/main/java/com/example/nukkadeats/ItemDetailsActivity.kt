package com.example.nukkadeats

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeats.databinding.ActivityItemDetailsBinding

class ItemDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityItemDetailsBinding

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

        val foodName = intent.getStringExtra("MenuItemName")
        val foodImage = intent.getIntExtra("MenuItemImage" , 0)

        binding.foodName.text = foodName
        binding.foodDescriptionImage.setImageResource(foodImage)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.addToCartBtn.setOnClickListener {

        }
    }
}