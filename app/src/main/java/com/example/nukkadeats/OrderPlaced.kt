package com.example.nukkadeats

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nukkadeats.Fragments.HomeFragment
import com.example.nukkadeats.databinding.FragmentOrderPlacedBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderPlaced : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentOrderPlacedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOrderPlacedBinding.inflate(inflater, container, false)

        binding.goToHome.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        return binding.root
    }


}