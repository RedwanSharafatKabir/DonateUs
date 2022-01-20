package com.donate.us.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.donate.us.internetcheck.CheckAvailableInternet
import com.donate.us.modelclasses.StoreUserData
import com.donate.us.activities.MainActivity
import com.donate.us.offlinedb.SharedPref
import com.donate.us.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPrefs: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPrefs = SharedPref()
        sharedPrefs.init(applicationContext)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("User Info")
        binding.registerProgressId.visibility = View.INVISIBLE

        binding.signUp.setOnClickListener{
            binding.registerProgressId.visibility = View.VISIBLE
            val username: String = binding.enterUsername.text.toString()
            val email: String = binding.enterEmail.text.toString()
            val password: String = binding.enterPassword.text.toString()
            val phone: String = binding.enterPhone.text.toString()

            if(email.isEmpty() || password.isEmpty() || phone.isEmpty()){
                Toast.makeText(applicationContext, "Enter all fields", Toast.LENGTH_SHORT).show()
                binding.registerProgressId.visibility = View.INVISIBLE
            }

            if(password.length<8){
                binding.enterPassword.error = "Minimum 8 characters"
                binding.registerProgressId.visibility = View.INVISIBLE
            }

            else {
                if (checkAvailableInternet.checkInternet(applicationContext)) {
                    signUpWithEmail(email, username, phone, password)

                } else {
                    binding.registerProgressId.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Turn on internet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.loginPage.setOnClickListener{
            super.onBackPressed()
        }

        binding.backPage.setOnClickListener{
            super.onBackPressed()
        }
    }

    private fun signUpWithEmail(email: String, username: String, phone: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                binding.registerProgressId.visibility = View.INVISIBLE
                                sharedPrefs.write("phoneKey", phone)
                                storeDataMethod(email, username, phone)

                                binding.enterUsername.setText("")
                                binding.enterEmail.setText("")
                                binding.enterPassword.setText("")
                                binding.enterPhone.setText("")

                                val it = Intent(this@RegisterActivity, MainActivity::class.java)
                                startActivity(it)
                                finish()

                            } else {
                                binding.registerProgressId.visibility = View.INVISIBLE
                                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                } else {
                    Toast.makeText(applicationContext, "SignUp failed", Toast.LENGTH_SHORT).show()
                    binding.registerProgressId.visibility = View.INVISIBLE
                }
            }
    }

    private fun storeDataMethod(email: String, username: String, phone: String) {
        val user: FirebaseUser? = auth.currentUser

        if (user != null) {
            val profile: UserProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(phone).build()
            user.updateProfile(profile).addOnCompleteListener {}
        }

        val storeUserData = StoreUserData(username, email, phone, "notSaved")
        databaseReference.child(phone).setValue(storeUserData)

        Toast.makeText(this@RegisterActivity, "Successfully Registered", Toast.LENGTH_SHORT).show()
    }
}
