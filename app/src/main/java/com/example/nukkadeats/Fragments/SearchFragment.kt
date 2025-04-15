package com.example.nukkadeats.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.R
import com.example.nukkadeats.adapters.MenuAdapter
import com.example.nukkadeats.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var adapter : MenuAdapter

    private val menuItemNameSearch : List<String> = listOf("Burger", "Pizza" , "Tatti", "Sydney Sweeney Kachi Ghani" , "Burger",
        "Pizza",
        "Sushi Platter",
        "Pasta Carbonara",
        "Chicken Curry",
        "Caesar Salad",
        "Chocolate Cake",
        "Ice Cream Sundae")

    private val menuItemDescriptionSearch : List<String> = listOf("Very premium quality Gupta Burger" , "Pizza with Parmesan cheese(Dhong hai dhong)" , "Dish of the year" , "Dish Only available for developer(Ayush Paliwal)" , "Classic beef burger with all the fixings.",
        "Large pepperoni pizza with extra cheese.",
        "Assortment of fresh sushi rolls.",
        "Creamy pasta with bacon and egg.",
        "Spicy chicken curry with rice.",
        "Crisp romaine lettuce with Caesar dressing.",
        "Rich and decadent chocolate cake.",
        "Vanilla ice cream with various toppings.")

    private val menuItemPriceSearch : List<String> = listOf("80", "120" , "1500", "999999" , "80", "120" , "1500", "999999" , "80", "120" , "1500", "999999")
    private val menuItemImageSearch : List<Int> = listOf(R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney , R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney , R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val filteredMenuFoodName = mutableListOf<String>()
    private val filteredMenuFoodDescription = mutableListOf<String>()
    private val filteredMenuFoodPrice = mutableListOf<String>()
    private val filteredMenuFoodImage = mutableListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container , false)
        // Inflate the layout for this fragment
//
//        adapter = MenuAdapter(filteredMenuFoodName , filteredMenuFoodDescription , filteredMenuFoodPrice , filteredMenuFoodImage , requireContext())
//        binding.viewMenuRecycler.layoutManager = LinearLayoutManager(requireContext())
//        binding.viewMenuRecycler.adapter = adapter

        setUpSearchView()

        //Show All Menu Items
        showAllMenu()

        return binding.root
    }

    private fun showAllMenu() {
        filteredMenuFoodName.clear()
        filteredMenuFoodDescription.clear()
        filteredMenuFoodPrice.clear()
        filteredMenuFoodImage.clear()

        filteredMenuFoodName.addAll(menuItemNameSearch)
        filteredMenuFoodDescription.addAll(menuItemDescriptionSearch)
        filteredMenuFoodPrice.addAll(menuItemPriceSearch)
        filteredMenuFoodImage.addAll(menuItemImageSearch)
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(p0: String): Boolean {
                filterMenuItems(p0)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        filteredMenuFoodName.clear()
        filteredMenuFoodDescription.clear()
        filteredMenuFoodPrice.clear()
        filteredMenuFoodImage.clear()

        menuItemNameSearch.forEachIndexed{index , foodName ->
            if(foodName.contains(query, ignoreCase = true)){
                filteredMenuFoodName.add(foodName)
                filteredMenuFoodDescription.add(menuItemDescriptionSearch[index])
                filteredMenuFoodPrice.add(menuItemPriceSearch[index])
                filteredMenuFoodImage.add(menuItemImageSearch[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

}