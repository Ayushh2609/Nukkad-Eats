package com.example.nukkadeats

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.Modal.OrderDetaild
import com.example.nukkadeats.adapters.recentBuyAdapter
import com.example.nukkadeats.databinding.ActivityRecentOrderItemsBinding

class recentOrderItems : AppCompatActivity() {
    private val binding : ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private lateinit var allFoodNames : ArrayList<String>
    private lateinit var allFoodPrices : ArrayList<String>
    private lateinit var allFoodImages : ArrayList<String>
    private lateinit var allFoodQuantities : ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        val recentOrderItems = intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetaild>
        recentOrderItems?.let {orderDetails ->
            if(orderDetails.isNotEmpty()){
                val recentOrderItem = orderDetails[0]

                allFoodNames = recentOrderItem.foodNames as ArrayList<String>
                allFoodPrices = recentOrderItem.foodPrices as ArrayList<String>
                allFoodQuantities = recentOrderItem.foodQuantities as ArrayList<Int>
                allFoodImages = recentOrderItem.foodImages as ArrayList<String>


            }
        }
        setAdapter()


    }

    private fun setAdapter() {

        val rv = binding.recentItemsRecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = recentBuyAdapter(this , allFoodNames , allFoodImages , allFoodPrices , allFoodQuantities)
        rv.adapter = adapter

    }
}