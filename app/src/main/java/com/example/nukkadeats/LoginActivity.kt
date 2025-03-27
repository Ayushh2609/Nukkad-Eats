package com.example.nukkadeats

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeats.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
private val binding : ActivityLoginBinding by lazy {
    ActivityLoginBinding.inflate(layoutInflater)
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        binding.dontHaveAccount.setOnClickListener{
            val intent = Intent(this , SignupActivity::class.java)
            startActivity(intent)
        }

    }
}