package com.example.nukkadeats.Fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nukkadeats.Modal.OrderDetaild
import com.example.nukkadeats.R
import com.example.nukkadeats.adapters.BuyAgainAdapter
import com.example.nukkadeats.databinding.FragmentHistoryBinding
import com.example.nukkadeats.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItems: MutableList<OrderDetaild> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)


        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()

        //Retreive Buy item history
        retriveBuyHistory()

        binding.moreDetails.setOnClickListener{
            seeItemsRecentBuy()
        }

        binding.receivedBottom.setOnClickListener{
            updateOrderStatus()
            Toast.makeText(requireContext() , "Order is Received" , Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItems[0].itemPushKey
        val completeOrderReference = database.reference.child("CompleteOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)

    }

    private fun seeItemsRecentBuy() {
        listOfOrderItems.firstOrNull()?.let {recentBuy ->
            val intent = Intent(requireContext() , recentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItem" , listOfOrderItems as Serializable)
            startActivity(intent)
        }
    }

    private fun retriveBuyHistory() {
        binding.recentlyBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference = database.getReference("users").child(userId).child("OrderHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapShot in snapshot.children) {
                    val buyHistoryItem = buySnapShot.getValue(OrderDetaild::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItems.add(it)
                    }
                }

                listOfOrderItems.reverse()

                if (listOfOrderItems.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun setDataInRecentBuyItem() {
        binding.recentlyBuyItem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItems.firstOrNull()
        recentOrderItem?.let {
            with(binding) {
                recentOrderName.text = it.foodNames?.firstOrNull() ?: ""

                val image = it.foodImages?.firstOrNull() ?: ""
                val uri = Uri.parse(image)

                Glide.with(requireContext()).load(uri).into(recentOrderImage)

                val isOrderAccepted = listOfOrderItems[0].orderAccepted
                if(isOrderAccepted){
                    receivedBottom.text = "Received"
                    receivedBottom.visibility = View.VISIBLE
                    receivedBottom.setTextColor(Color.parseColor("#FF0000"))
                }
            }
        }
    }

    private fun setPreviousBuyItemRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()

        for (i in 1 until listOfOrderItems.size) {
            listOfOrderItems[i].foodNames?.firstOrNull()?.let { buyAgainFoodName.add(it) }
            listOfOrderItems[i].foodPrices?.firstOrNull()?.let { buyAgainFoodPrice.add(it) }
            listOfOrderItems[i].foodImages?.firstOrNull()?.let { buyAgainFoodImage.add(it) }

        }
        if (buyAgainFoodName.isNotEmpty()) {
            val rv = binding.buyAgainRecyclerView
            rv.layoutManager = LinearLayoutManager(requireContext())
            buyAgainAdapter = BuyAgainAdapter(
                buyAgainFoodName,
                buyAgainFoodPrice,
                buyAgainFoodImage,
                requireContext()
            )
            rv.adapter = buyAgainAdapter

        }


    }
}