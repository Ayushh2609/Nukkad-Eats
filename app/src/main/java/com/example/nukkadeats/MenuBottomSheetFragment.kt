package com.example.nukkadeats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.Modal.MenuItemModal
import com.example.nukkadeats.adapters.MenuAdapter
import com.example.nukkadeats.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItemModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        retrieveMenuItems()

        return binding.root
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val menuRef: DatabaseReference = database.reference.child("menu")

        menuItems = mutableListOf()

        menuRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItemModal::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                    Log.d("adapter" , "Data received")

                    //Once data receive, set to adapter
                    setAdapter()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun setAdapter() {

        if (menuItems.isNotEmpty()) {

            val adapter = MenuAdapter(menuItems, requireContext())
            binding.viewMenuRecycler.layoutManager = LinearLayoutManager(requireContext())
            binding.viewMenuRecycler.adapter = adapter
            Log.d("adapter" , "Data Set")
        }else{
            Log.d("adapter" , "Data not Set")
        }

    }


}