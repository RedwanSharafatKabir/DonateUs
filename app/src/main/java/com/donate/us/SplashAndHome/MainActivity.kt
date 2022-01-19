package com.donate.us.SplashAndHome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.donate.us.Authentication.LoginActivity
import com.donate.us.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.Context
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.donate.us.R

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
    }

    override fun onBackPressed() {
        val alertDialogBuilder: AlertDialog.Builder
        alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilder.setTitle("EXIT !")
        alertDialogBuilder.setMessage("Are you sure you want to close this app ?")
        alertDialogBuilder.setIcon(R.drawable.exit)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            "Yes"
        ) { dialog, which ->
            val nullValue = ""
            setNullMethod(nullValue)

            finish()
            finishAffinity()
        }

        alertDialogBuilder.setNeutralButton(
            "No"
        ) { dialog, which -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun setNullMethod(nullValue: String) {
        try {
            val fileOutputStream: FileOutputStream = openFileOutput("phoneKey.txt", Context.MODE_PRIVATE)
            fileOutputStream.write(nullValue.toByteArray())
            fileOutputStream.close()

            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
