package com.donate.us.SplashAndHome

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.donate.us.Authentication.LoginActivity
import com.donate.us.R
import com.donate.us.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.getStartedFloatingId.setOnClickListener(View.OnClickListener {
            finish()
            val intent = Intent(this@SplashScreen, LoginActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onBackPressed() {
        val alertDialogBuilder: AlertDialog.Builder
        alertDialogBuilder = AlertDialog.Builder(this@SplashScreen)
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
