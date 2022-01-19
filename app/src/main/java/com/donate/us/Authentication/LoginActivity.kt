package com.donate.us.Authentication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.donate.us.InternetCheck.CheckAvailableInternet
import com.donate.us.R
import com.donate.us.SplashAndHome.MainActivity
import com.donate.us.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private var currentUserPhone: String = ""
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("User Info")
        binding.loginProgressId.visibility = View.INVISIBLE

        val getUserPhone: EditText = binding.enterUserPhone
        val getPassword: EditText = binding.enterPasswordLogin

        binding.buttonConfirm.setOnClickListener(View.OnClickListener {
            binding.loginProgressId.visibility = View.VISIBLE
            phone = getUserPhone.text.toString()
            password = getPassword.text.toString()

            if(phone.isEmpty()){
                getUserPhone.setError("Enter Phone Number")
                binding.loginProgressId.visibility = View.INVISIBLE
            }

            if(phone.length>0 && phone.length<11){
                getUserPhone.setError("Invalid Phone Number")
                binding.loginProgressId.visibility = View.INVISIBLE
            }

            if(password.isEmpty()){
                getPassword.setError("Enter Password")
                binding.loginProgressId.visibility = View.INVISIBLE
            }

            else {
                if (checkAvailableInternet.checkInternet(applicationContext)) {
                    // Access User Email
                    try {
                        databaseReference.child(phone).child("userEmail").addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    try {
                                        email = snapshot.value.toString()
                                        auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                                                if (task.isSuccessful) {
                                                    binding.loginProgressId.visibility = View.INVISIBLE
                                                    rememberMethod(phone)

                                                    getUserPhone.setText("")
                                                    getPassword.setText("")

                                                    val it = Intent(this@LoginActivity, MainActivity::class.java)
                                                    startActivity(it)
                                                    finish()

                                                } else {
                                                    binding.loginProgressId.visibility = View.INVISIBLE
                                                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                                                }
                                            })

                                    } catch (e: Exception) {
                                        binding.loginProgressId.visibility = View.INVISIBLE
                                        Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                                        Log.i("DB_Error", e.message.toString())
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    binding.loginProgressId.visibility = View.INVISIBLE
                                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                                    Log.i("DB_Error", error.message)
                                }
                            })

                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                        binding.loginProgressId.visibility = View.INVISIBLE
                    }

                } else {
                    binding.loginProgressId.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Turn on internet", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.signUpPage.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        })
    }

    private fun checkCurrentUser() {
        try {
            var recievedMessageTc: String?
            val fileInputStreamTc = openFileInput("phoneKey.txt")
            val inputStreamReaderTc = InputStreamReader(fileInputStreamTc)
            val bufferedReaderTc = BufferedReader(inputStreamReaderTc)
            val stringBufferTc = StringBuffer()
            while (bufferedReaderTc.readLine().also { recievedMessageTc = it } != null) {
                stringBufferTc.append(recievedMessageTc)
            }

            currentUserPhone = stringBufferTc.toString()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
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

    override fun onStart() {
        super.onStart()
        checkCurrentUser()
        if(currentUserPhone.isNotEmpty()){
            finish()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val alertDialogBuilder: AlertDialog.Builder
        alertDialogBuilder = AlertDialog.Builder(this@LoginActivity)
        alertDialogBuilder.setTitle("EXIT !")
        alertDialogBuilder.setMessage("Are you sure you want to close this app ?")
        alertDialogBuilder.setIcon(R.drawable.exit)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            "Yes"
        ) { dialog, which ->
            finish()
            finishAffinity()
        }

        alertDialogBuilder.setNeutralButton(
            "No"
        ) { dialog, which -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
