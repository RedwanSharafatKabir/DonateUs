package com.donate.us.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.donate.us.internetcheck.CheckAvailableInternet
import com.donate.us.R
import com.donate.us.activities.MainActivity
import com.donate.us.offlinedb.SharedPref
import com.donate.us.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private var currentUserPhone: String = ""
    private lateinit var userReference: DatabaseReference
    private lateinit var adminReference: DatabaseReference
    private lateinit var sharedPrefs: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPrefs = SharedPref()
        sharedPrefs.init(applicationContext)

        auth = FirebaseAuth.getInstance()
        userReference = FirebaseDatabase.getInstance().getReference("User Info")
        adminReference = FirebaseDatabase.getInstance().getReference("Admin Info")
        binding.loginProgressId.visibility = View.INVISIBLE

        val getUserPhone: EditText = binding.enterUserPhone
        val getPassword: EditText = binding.enterPasswordLogin

        binding.buttonConfirm.setOnClickListener{
            binding.loginProgressId.visibility = View.VISIBLE
            phone = getUserPhone.text.toString()
            password = getPassword.text.toString()

            if(phone.isEmpty()){
                getUserPhone.error = "Enter Phone Number"
                binding.loginProgressId.visibility = View.INVISIBLE
            }

            if(phone.length<11){
                getUserPhone.error = "Invalid Phone Number"
                binding.loginProgressId.visibility = View.INVISIBLE
            }

            if(password.isEmpty()){
                getPassword.error = "Enter Password"
                binding.loginProgressId.visibility = View.INVISIBLE
            }

            else {
                if (checkAvailableInternet.checkInternet(applicationContext)) {
                    // Access User Email
                    try {
                        userReference.child(phone).child("userEmail").addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    try {
                                        email = snapshot.value.toString()
                                        auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener{ task ->
                                                if (task.isSuccessful) {
                                                    binding.loginProgressId.visibility = View.INVISIBLE
                                                    sharedPrefs.write("phoneKey", phone)
                                                    sharedPrefs.write("userTypeKey", "Donor")

                                                    getUserPhone.setText("")
                                                    getPassword.setText("")

                                                    val it = Intent(this@LoginActivity, MainActivity::class.java)
                                                    startActivity(it)
                                                    finish()

                                                } else {
                                                    binding.loginProgressId.visibility = View.INVISIBLE
                                                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                                                }
                                            }

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
                        try {
                            adminReference.child(phone).child("userEmail").addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    try {
                                        email = snapshot.value.toString()
                                        auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener{ task ->
                                                if (task.isSuccessful) {
                                                    binding.loginProgressId.visibility = View.INVISIBLE
                                                    sharedPrefs.write("phoneKey", phone)
                                                    sharedPrefs.write("userTypeKey", "Admin")

                                                    getUserPhone.setText("")
                                                    getPassword.setText("")

                                                    val it = Intent(this@LoginActivity, MainActivity::class.java)
                                                    startActivity(it)
                                                    finish()

                                                } else {
                                                    binding.loginProgressId.visibility = View.INVISIBLE
                                                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                                                }
                                            }

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
                    }

                } else {
                    binding.loginProgressId.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Turn on internet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.signUpPage.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        currentUserPhone = sharedPrefs.read("phoneKey", "").toString()

        if(currentUserPhone.isNotEmpty()){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
        alertDialogBuilder.setTitle("EXIT !")
        alertDialogBuilder.setMessage("Are you sure you want to close this app ?")
        alertDialogBuilder.setIcon(R.drawable.exit)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            "Yes"
        ) { _, _ ->
            finish()
            finishAffinity()
        }

        alertDialogBuilder.setNeutralButton(
            "No"
        ) { dialog, _ -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
