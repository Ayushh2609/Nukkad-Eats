package com.example.nukkadeats

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import androidx.core.view.WindowInsetsCompat
import com.example.nukkadeats.databinding.ActivitySignupBinding
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var username : String
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var callbackManager : CallbackManager

    private val binding : ActivitySignupBinding by lazy{
        ActivitySignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        //Initialize Firebase Auth
        auth = Firebase.auth

        //Inititalize Database
        database = Firebase.database.reference

        binding.alreadyHaveAccount.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.signUpCreateButton.setOnClickListener{
            username = binding.username.text.toString().trim()
            email = binding.emailId.text.toString().trim()
            password = binding.passwd.text.toString().trim()

            if(username.isBlank()){
                binding.username.error = "Please provide the username"
            }
            if(email.isBlank()){
                binding.emailId.error = "Please provide the email"
            }
            if(password.isBlank()){
                binding.passwd.error = "Please enter the password"
            }
            else{
                createAccount(email , password)
            }
        }
    }

    private fun createAccount(email : String , password : String) {
        auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener { task ->

            if(task.isSuccessful){
                saveUserData()
                Toast.makeText(this , "Account creation successful" , Toast.LENGTH_SHORT).show()
                startActivity(Intent(this , LoginActivity::class.java))
                finish()
            }else{
                Toast.makeText(this , "Account creation failed" , Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveUserData() {

    }
}