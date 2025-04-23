package com.example.nukkadeats

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.nukkadeats.Modal.UserModal
import com.example.nukkadeats.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private val callbackManager: CallbackManager = CallbackManager.Factory.create()

    private val binding: ActivityLoginBinding by lazy {
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


        binding.loginBtn.setOnClickListener {
            email = binding.emailId.text.toString().trim()
            password = binding.passwd.text.toString().trim()

            if (email.isBlank()) {
                binding.emailId.error = "Please provide email"
            }
            if (password.isBlank()) {
                binding.passwd.error = "Please enter the password"
            } else {
                createUser()
            }
        }


        //Google SignIn Options Initialize
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.googleButton.setOnClickListener {
            val signinIntent = googleSignInClient.signInIntent
            launcher.launch(signinIntent)
        }

        binding.dontHaveAccount.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            val user = auth.currentUser
                            Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT)
                                .show()

                            //Saving User data to Firebase
                            saveUserdata(user?.displayName , user?.email , null , "Google")

                            //Successfully Sign in with Google
                            updateUi(authTask.result?.user)
                            finish()
                        } else {
                            Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Account creation failed", Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
                updateUi(user)
            } else {
                Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SignupActivity::class.java))
            }
        }
    }

    //Saving data through Google and facebook
    private fun saveUserdata(name : String? , email : String? , password : String? , loginMethod : String?){

        val userid = FirebaseAuth.getInstance().currentUser?.uid?: return
        val user = UserModal(name = name , email = email , password = password , loginMethod = loginMethod)

        userid?.let {
            database.child("users").child(it).setValue(user)
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUi(currentUser)
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}