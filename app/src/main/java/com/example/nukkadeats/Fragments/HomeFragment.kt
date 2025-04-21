package com.example.nukkadeats.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.nukkadeats.MenuBottomSheetFragment
import com.example.nukkadeats.Modal.MenuItemModal
import com.example.nukkadeats.R
import com.example.nukkadeats.adapters.Popular_Item_Recycler_Adapter
import com.example.nukkadeats.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItemModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(layoutInflater , container , false)

        binding.viewMenubtn.setOnClickListener{
            val bottonSheetDialog = MenuBottomSheetFragment()
            bottonSheetDialog.show(parentFragmentManager , "Test")
        }

        return binding.root
    }

    private fun retrieveAndDisplayPopularMenuItem() {
        // Get the database reference
        database = FirebaseDatabase.getInstance()
        val foodref : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        // Retrieve database
        foodref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItemModal::class.java)
                    menuItem?.let{menuItems.add(it)}
                }
                //Display the random popular item
                randomPopularItem()
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
            private fun randomPopularItem() {
                //Create as shuffled list of Menu Item
                val index = menuItems.indices.toList().shuffled()
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.img1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img4, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList , ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected item is $position"

                Toast.makeText(requireContext() , itemMessage , Toast.LENGTH_SHORT).show()
            }
        })

        val foodNames = listOf("Burger", "Pizza" , "Tatti", "Sydney Sweeney Kachi Ghani")
        val foodPrice = listOf("80", "120" , "1500", "999999")
        val foodImage= listOf(R.drawable.burger , R.drawable.pizza , R.drawable.poop , R.drawable.sydney)
        val foodDescriptions = listOf("Very premium quality Gupta Burger" , "Pizza with Parmesan cheese(Dhong hai dhong)" , "Dish of the year" , "Dish Only available for developer(Ayush Paliwal)")

        val adapter = Popular_Item_Recycler_Adapter(foodNames , foodPrice , foodImage , foodDescriptions , requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter
    }

}