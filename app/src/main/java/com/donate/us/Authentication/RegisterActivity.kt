package com.donate.us.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.donate.us.InternetCheck.CheckAvailableInternet
import com.donate.us.ModelClasses.StoreUserData
import com.donate.us.SplashAndHome.MainActivity
import com.donate.us.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.io.FileNotFoundException
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("User Info")
        binding.registerProgressId.visibility = View.INVISIBLE

        binding.signUp.setOnClickListener(View.OnClickListener {
            binding.registerProgressId.visibility = View.VISIBLE
            val username: String = binding.enterUsername.text.toString()
            val email: String = binding.enterEmail.text.toString()
            val password: String = binding.enterPassword.text.toString()
            val phone: String = binding.enterPhone.text.toString()

            if(email.isEmpty() || password.isEmpty() || phone.isEmpty()){
                Toast.makeText(applicationContext, "Enter all fields", Toast.LENGTH_SHORT).show()
            }

            if(password.length<8){
                binding.enterPassword.setError("Minimum 8 characters")
            }

            else {
                if (checkAvailableInternet.checkInternet(applicationContext)) {
                    signUpWithEmail(email, username, phone, password)

                } else {
                    binding.registerProgressId.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Turn on internet", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.loginPage.setOnClickListener(View.OnClickListener {
            super.onBackPressed()
        })

        binding.backPage.setOnClickListener(View.OnClickListener {
            super.onBackPressed()
        })
    }

    private fun signUpWithEmail(email: String, username: String, phone: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                            if (task.isSuccessful) {
                                binding.registerProgressId.visibility = View.INVISIBLE
                                rememberMethod(phone)
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
                        })

                } else {
                    Toast.makeText(applicationContext, "SignUp failed", Toast.LENGTH_SHORT).show()
                    binding.registerProgressId.visibility = View.INVISIBLE
                }
            }
    }

    private fun rememberMethod(phone: String) {
        try {
            val fileOutputStream = openFileOutput("phoneKey.txt", MODE_PRIVATE)
            fileOutputStream.write(phone.toByteArray())
            fileOutputStream.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun storeDataMethod(email: String, username: String, phone: String) {
        val user: FirebaseUser? = auth.getCurrentUser()

        if (user != null) {
            val profile: UserProfileChangeRequest
            profile = UserProfileChangeRequest.Builder().setDisplayName(phone).build()
            user.updateProfile(profile).addOnCompleteListener { task: Task<Void?>? -> }
        }

        val storeUserData = StoreUserData(username, email, phone)
        databaseReference.child(phone).setValue(storeUserData)

        Toast.makeText(this@RegisterActivity, "Successfully Registered", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
