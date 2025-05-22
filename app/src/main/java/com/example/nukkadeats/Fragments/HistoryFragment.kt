package com.example.nukkadeats.Fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nukkadeats.Modal.OrderDetaild
import com.example.nukkadeats.R
import com.example.nukkadeats.adapters.BuyAgainAdapter
import com.example.nukkadeats.databinding.FragmentHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {

    private lateinit var binding : FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter

    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var userId : String
    private var listOfOrderItems : MutableList<OrderDetaild> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHistoryBinding.inflate(layoutInflater , container , false)
        // Inflate the layout for this fragment
        setBuyAgainAdapter()

        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()

        //Retreive Buy item history
        retriveBuyHistory()


        return binding.root
    }

    private fun retriveBuyHistory() {
        binding.recentlyBuyItem.visibility = View.GONE
        userId = auth.currentUser?.uid?:""

        val buyItemReference = database.getReference("Users").child(userId).child("OrderHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(buySnapShot in snapshot.children){
                    val buyHistoryItem = buySnapShot.getValue(OrderDetaild ::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItems.add(it)
                    }
                }

                listOfOrderItems.reverse()

                if(listOfOrderItems.isNotEmpty()){
                    setDataInRecentBuyItem()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setDataInRecentBuyItem() {
        binding.recentlyBuyItem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItems.firstOrNull()
        recentOrderItem?.let {
            with(binding){
                recentOrderName.text = it.foodNames?.firstOrNull()?:""

                val image = it.foodImages?.firstOrNull()?:""
                val uri = Uri.parse(image)

                Glide.with(requireContext()).load(uri).into(recentOrderImage)
            }
        }
    }

    private fun setBuyAgainAdapter() {
        val buyAgainFoodName = arrayListOf("Burger", "Pizza" , "Tatti", "Sydney Sweeney Kachi Ghani" , "Burger",
            "Pizza",
            "Sushi Platter",
            "Pasta Carbonara",
            "Chicken Curry",
            "Caesar Salad",
            "Chocolate Cake",
            "Ice Cream Sundae")

        val buyAgainFoodDescription = arrayListOf("Very premium quality Gupta Burger" , "Pizza with Parmesan cheese(Dhong hai dhong)" , "Dish of the year" , "Dish Only available for developer(Ayush Paliwal)" , "Classic beef burger with all the fixings.",
            "Large pepperoni pizza with extra cheese.",
            "Assortment of fresh sushi rolls.",
            "Creamy pasta with bacon and egg.",
            "Spicy chicken curry with rice.",
            "Crisp romaine lettuce with Caesar dressing.",
            "Rich and decadent chocolate cake.",
            "Vanilla ice cream with various toppings.")

        val buyAgainFoodPrice = arrayListOf("80", "120" , "1500", "999999" , "80", "120" , "1500", "999999" , "80", "120" , "1500", "999999")

        val buyAgainFoodImage = arrayListOf(R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney , R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney , R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney)


        buyAgainAdapter = BuyAgainAdapter(buyAgainFoodName , buyAgainFoodDescription , buyAgainFoodPrice , buyAgainFoodImage)
        binding.buyagainRecyclerView.adapter = buyAgainAdapter
        binding.buyagainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}