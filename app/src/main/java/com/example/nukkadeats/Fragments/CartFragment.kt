package com.example.nukkadeats.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.R
import com.example.nukkadeats.adapters.cartAdapter
import com.example.nukkadeats.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var binding : FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater , container , false)
        // Inflate the layout for this fragment

        val foodNames = listOf("Burger", "Pizza" , "Tatti", "Sydney Sweeney Kachi Ghani")
        val foodPrice = listOf("80", "120" , "1500", "999999")
        val foodImage= listOf(R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney)

        val adapter = cartAdapter(ArrayList(foodNames) , ArrayList(foodPrice) , ArrayList(foodImage))
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        return binding.root
    }

}