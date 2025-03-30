package com.example.nukkadeats.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nukkadeats.R
import com.example.nukkadeats.adapters.NotificationAdapter
import com.example.nukkadeats.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class notification_bottom : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater , container , false)
        val notificationMessage = listOf("Your order has been Canceled Successfully"
                                                , "Order has been taken by the driver"
                                                , "Congrats Your Order Placed")

        val notificationImage = listOf(R.drawable.sademoji , R.drawable.dilevery_icon , R.drawable.green_tick)

        val adapter = NotificationAdapter(ArrayList(notificationMessage), ArrayList(notificationImage))
        binding.notificationRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecycler.adapter = adapter

        return binding.root
    }

}