package com.example.nukkadeats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.adapters.MenuAdapter
import com.example.nukkadeats.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentMenuBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMenuBottomSheetBinding.inflate(inflater , container , false)

        val allMenuNames = listOf("Burger", "Pizza" , "Tatti", "Sydney Sweeney Kachi Ghani" , "Burger",
            "Pizza",
            "Sushi Platter",
            "Pasta Carbonara",
            "Chicken Curry",
            "Caesar Salad",
            "Chocolate Cake",
            "Ice Cream Sundae")
        val allMenuDescription = listOf("Very premium quality Gupta Burger" , "Pizza with Parmesan cheese(Dhong hai dhong)" , "Dish of the year" , "Dish Only available for developer(Ayush Paliwal)" , "Classic beef burger with all the fixings.",
            "Large pepperoni pizza with extra cheese.",
            "Assortment of fresh sushi rolls.",
            "Creamy pasta with bacon and egg.",
            "Spicy chicken curry with rice.",
            "Crisp romaine lettuce with Caesar dressing.",
            "Rich and decadent chocolate cake.",
            "Vanilla ice cream with various toppings.")
        val allMenuPrice = listOf("80", "120" , "1500", "999999" , "80", "120" , "1500", "999999" , "80", "120" , "1500", "999999")
        val allMenuimage = listOf(R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney , R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney , R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney)

        val adapter = MenuAdapter(ArrayList(allMenuNames) , ArrayList(allMenuDescription) , ArrayList(allMenuPrice) , ArrayList(allMenuimage) , requireContext())
        binding.viewMenuRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.viewMenuRecycler.adapter = adapter

        return binding.root
    }

}