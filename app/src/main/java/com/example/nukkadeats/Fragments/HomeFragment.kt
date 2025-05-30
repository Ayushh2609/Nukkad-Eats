package com.example.nukkadeats.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import retrofit2.Callback
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.nukkadeats.MenuBottomSheetFragment
import com.example.nukkadeats.Modal.MenuItemModal
import com.example.nukkadeats.Modal.User
import com.example.nukkadeats.R
import com.example.nukkadeats.adapters.MenuAdapter
import com.example.nukkadeats.databinding.FragmentHomeBinding
import com.example.nukkadeats.network.RetrofitInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mindorks.example.shimmer.MainAdapter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItemModal>

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    private fun setupAPICall() {
        // Retrofit API call
        val apiService = RetrofitInstance.apiService  // Use the apiService from RetrofitInstance
        val call = apiService.getUsers() // Get the Call object
        call.enqueue(object : Callback<List<User>> {  // Use Retrofit's Callback

            override fun onResponse(
                call: retrofit2.Call<List<User>>,
                response: retrofit2.Response<List<User>>
            ) {
                // Handle the response
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        // Update the UI on the main thread
                        try {
                            binding.shimmerFrameLayout.stopShimmerAnimation()
                            binding.shimmerFrameLayout.visibility = View.GONE
                            binding.popularRecyclerView.visibility = View.VISIBLE // Use binding
                            adapter.addData(users)
                            adapter.notifyDataSetChanged()
                        } catch (e: Exception) {
                            Log.e("HomeFragment", "Error updating UI: ", e)
                            Toast.makeText(
                                requireContext(),
                                "Error updating UI",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle null response body
                        val errorMessage = "Empty user list received"
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                        Log.w("API_RESPONSE", errorMessage)
                        binding.shimmerFrameLayout.stopShimmerAnimation()
                        binding.shimmerFrameLayout.visibility = View.GONE
                    }
                } else {
                    // Handle unsuccessful response (e.g., 404, 500)
                    val errorMessage = "Error: ${response.code()} - ${response.message()}"
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    Log.e("API_RESPONSE", errorMessage)
                    binding.shimmerFrameLayout.stopShimmerAnimation()
                    binding.shimmerFrameLayout.visibility = View.GONE
                }
            }

            override fun onFailure(call: retrofit2.Call<List<User>>, t: Throwable) {
                // Handle network errors
                val errorMessage = "Network error: ${t.message}"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                Log.e("API_ERROR", errorMessage, t)
                binding.shimmerFrameLayout.stopShimmerAnimation()
                binding.shimmerFrameLayout.visibility = View.GONE
            }
        })
    }

    private fun setupUI() {
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainAdapter(arrayListOf())
        binding.popularRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.popularRecyclerView.context,
                (binding.popularRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.popularRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerFrameLayout.startShimmerAnimation()
    }

    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmerAnimation()
        super.onPause()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.viewMenubtn.setOnClickListener {
            val bottonSheetDialog = MenuBottomSheetFragment()
            bottonSheetDialog.show(parentFragmentManager, "Test")
        }

        //Retrieve and display popular menu item
        retrieveAndDisplayPopularMenuItem()

        return binding.root
    }

    private fun retrieveAndDisplayPopularMenuItem() {
        // Get the database reference
        database = FirebaseDatabase.getInstance()
        val foodref: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        // Retrieve database
        foodref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItemModal::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                //Display the random popular item
                randomPopularItem()
            }


            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun randomPopularItem() {
        //Create as shuffled list of Menu Item
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 6
        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }

        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItemModal>) {
        val adapter = MenuAdapter(subsetMenuItems, requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupAPICall()

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.img1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img4, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected item is $position"

                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

}