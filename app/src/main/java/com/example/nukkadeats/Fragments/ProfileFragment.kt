package com.example.nukkadeats.Fragments

import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nukkadeats.Modal.UserModal
import com.example.nukkadeats.R
import com.example.nukkadeats.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        setUserData()

        binding.saveInfoProfile.setOnClickListener {

            val name = binding.nameProfile.text.toString()
            val address = binding.addressProfile.text.toString()
            val email = binding.emailProfile.text.toString()
            val phone = binding.phoneProfile.text.toString()

            updateUserData(name, address, email, phone)
        }

        return binding.root
    }

    private fun updateUserData(name: String, address: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = database.reference.child("users").child(userId)

            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userRef.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile updated Successfully", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Profile updated failed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid

        if (userId != null) {

            val userRef = database.reference.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val userProfile = snapshot.getValue(UserModal::class.java)
                        if (userProfile != null) {
                            binding.nameProfile.setText(userProfile.name)
                            binding.addressProfile.setText(userProfile.address)
                            binding.emailProfile.setText(userProfile.email)
                            binding.phoneProfile.setText(userProfile.phone)

                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }


    }
}