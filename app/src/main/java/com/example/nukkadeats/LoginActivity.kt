package com.example.nukkadeats

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.nukkadeats.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

        //Initialize Firebase Auth
        auth = Firebase.auth

        //Initialize Firebase Database
        database = Firebase.database.reference

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Default_web_client_id)).requestEmail().build()

        val googleSignInClient = GoogleSignIn.getClient(this , googleSignInOptions)

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
                createUser()
            }
        }

        binding.dontHaveAccount.setOnClickListener{
            val intent = Intent(this , SignupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email , password).addOnCompleteListener {task ->
            if(task.isSuccessful){
                val user = auth.currentUser
                Toast.makeText(this , "Welcome ${user?.displayName}" , Toast.LENGTH_SHORT).show()
                updateUi(user)
            }else{
                Toast.makeText(this , "User Not Found" , Toast.LENGTH_SHORT).show()
                startActivity(Intent(this , SignupActivity::class.java))
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this , MainActivity::class.java))
        finish()
    }
}