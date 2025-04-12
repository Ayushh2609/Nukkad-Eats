package com.example.nukkadeats

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.example.nukkadeats.Modal.UserModal
import com.example.nukkadeats.databinding.ActivitySignupBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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

        //
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Default_web_client_id)).requestEmail().
            build()

        //Google Initialize
        googleSignInClient = GoogleSignIn.getClient(this , googleSignInOptions)

        //Already have account
        binding.alreadyHaveAccount.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        //Google Button
        binding.googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
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

    //Launcher for Google
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken , null)

                auth.signInWithCredential(credential).addOnCompleteListener {authTask->
                    if(task.isSuccessful){
                        val user = auth.currentUser
                        Toast.makeText(this , "Welcome ${user?.displayName}" , Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this , MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this , "Account creation failed" , Toast.LENGTH_LONG).show()
                        Log.d("AccountFail" , "createAccount: ${task.exception}")
                    }
                }
            }else{
                Toast.makeText(this , "Account creation failed" , Toast.LENGTH_LONG).show()
            }
        }

    }

    //Account creation with Email and Password
    private fun createAccount(email : String , password : String) {
        auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener { task ->

            if(task.isSuccessful){
                saveUserData()
                Toast.makeText(this , "Account creation successful" , Toast.LENGTH_SHORT).show()
                startActivity(Intent(this , LoginActivity::class.java))
                finish()
            }else{
                Toast.makeText(this , "Account creation failed" , Toast.LENGTH_LONG).show()
                Log.d("AccountFail" , "createAccount: ${task.exception}")
            }
        }
    }

    //Saving Data manually
    private fun saveUserData() {

        username = binding.username.text.toString()
        email = binding.emailId.text.toString().trim()
        password = binding.passwd.text.toString().trim()

        val user = UserModal(email , username , password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        //Saving data to Users node in Firebase Database
        database.child("users").child(userId).setValue(user)
    }
}