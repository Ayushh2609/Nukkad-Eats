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
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FacebookAuthProvider.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

        //Initialize CallBackManager
        callbackManager = CallbackManager.Factory.create()

        //Facebook Button
        binding.facebookButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                listOf("email", "public_profile")
            )

            LoginManager.getInstance().registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("Success", "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d("OnCancel", "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("OnError", "facebook:onError", error)
                }
            })
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
                    if(authTask.isSuccessful){
                        val user = auth.currentUser
                        Toast.makeText(this , "Welcome ${user?.displayName}" , Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this , MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this , "Account creation failed" , Toast.LENGTH_SHORT).show()
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

        val user = UserModal(username , email  , password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        //Saving data to Users node in Firebase Database
        database.child("users").child(userId).setValue(user)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")

        val credential = getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is successfully authenticated
            startActivity(Intent(this , MainActivity::class.java))
            finish()
        } else {
            // Authentication failed, keep the user on the current screen.
            Log.d("TAG", "Authentication failed, not proceeding to MainActivity.")
        }
    }
}