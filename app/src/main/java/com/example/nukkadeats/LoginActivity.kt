package com.example.nukkadeats

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.nukkadeats.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
private val binding : ActivityLoginBinding by lazy {
    ActivityLoginBinding.inflate(layoutInflater)
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this , MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.dontHaveAccount.setOnClickListener{
            val intent = Intent(this , SignupActivity::class.java)
            startActivity(intent)
        }

    }
}