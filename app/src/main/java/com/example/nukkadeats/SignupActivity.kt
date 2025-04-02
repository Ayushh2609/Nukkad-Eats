package com.example.nukkadeats

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeats.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private val binding : ActivitySignupBinding by lazy{
        ActivitySignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        binding.alreadyHaveAccount.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.signUpCreateButton.setOnClickListener{
            val intent = Intent(this , ChooseLocationActivity::class.java)
            startActivity(intent)
        }
    }
}