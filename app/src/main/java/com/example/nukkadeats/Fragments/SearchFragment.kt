package com.example.nukkadeats.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.Modal.MenuItemModal
import com.example.nukkadeats.adapters.MenuAdapter
import com.example.nukkadeats.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter

    private lateinit var database: FirebaseDatabase
    private val originalMenuItem = mutableListOf<MenuItemModal>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        //Retrieve menu item from Database
        retrieveMenuItem()

        setUpSearchView()

        return binding.root
    }

    private fun retrieveMenuItem() {
        //getDatabase reference
        database = FirebaseDatabase.getInstance()

        //Reference to the data node
        val foodReference: DatabaseReference = database.reference.child("menu")
        foodReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItemModal::class.java)
                    menuItem?.let {
                        originalMenuItem.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(originalMenuItem)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<MenuItemModal>) {
        adapter = MenuAdapter(filteredMenuItem, requireContext())
        binding.viewMenuRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.viewMenuRecycler.adapter = adapter
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        val filteredMenuItems = originalMenuItem.filter {
            it.foodName?.contains(query, ignoreCase = true) == true
        }
        setAdapter(filteredMenuItems)
    }
}