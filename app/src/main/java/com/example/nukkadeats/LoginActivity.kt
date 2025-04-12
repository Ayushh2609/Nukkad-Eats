package com.example.nukkadeats

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.nukkadeats.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class LoginActivity : AppCompatActivity() {
    private lateinit var email : String
    private  lateinit var password : String
    private  lateinit var auth : FirebaseAuth
    private  lateinit var database : DatabaseReference
    private lateinit var googleSignInClient : GoogleSignInClient

private val binding : ActivityLoginBinding by lazy {
    ActivityLoginBinding.inflate(layoutInflater)
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {

            email = binding.emailId.text.toString().trim()
            password = binding.passwd.text.toString().trim()

            if(email.isBlank()){
                binding.emailId.error = "Please provide email"
            }
            if(password.isBlank()){
                binding.passwd.error = "Please enter the password"
            }
            else{

            }
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