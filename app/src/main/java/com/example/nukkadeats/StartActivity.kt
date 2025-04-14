package com.example.nukkadeats

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class StartActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_start)

        val nextButton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.nextButton)

        nextButton.setOnClickListener{
            val intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
        }


    }

//    override fun onStart() {
//        super.onStart()
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            updateUi(currentUser)
//        }
//    }
//    private fun updateUi(user: FirebaseUser?) {
//        startActivity(Intent(this , MainActivity::class.java))
//        finish()
//    }


}