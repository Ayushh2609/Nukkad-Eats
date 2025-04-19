package com.example.nukkadeats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nukkadeats.Fragments.notification_bottom
import com.example.nukkadeats.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        var NavController = findNavController(R.id.fragmentContainerView)
        val bottomNavController = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavController.setupWithNavController(NavController)

        binding.notificationBtn.setOnClickListener {
            val BottomSheetDialog = notification_bottom()
            BottomSheetDialog.show(supportFragmentManager, "Test")
        }
    }
}