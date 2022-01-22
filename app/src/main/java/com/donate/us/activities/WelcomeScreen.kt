package com.donate.us.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.donate.us.authentication.LoginActivity
import com.donate.us.R
import com.donate.us.animation.Typewriter
import com.donate.us.databinding.ActivitySplashScreenBinding

class WelcomeScreen: AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.getStartedFloatingId.setOnClickListener {
            finish()
            val intent = Intent(this@WelcomeScreen, LoginActivity::class.java)
            startActivity(intent)
        }

        val writer1: Typewriter = binding.foodWaste
        writer1.setCharacterDelay(200)
        writer1.animateText(resources.getString(R.string.foodWaste))

        val writer2: Typewriter = binding.management
        writer2.setCharacterDelay(200)
        writer2.animateText(resources.getString(R.string.management))
    }

    override fun onBackPressed() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@WelcomeScreen)
        alertDialogBuilder.setTitle("EXIT !")
        alertDialogBuilder.setMessage("Are you sure you want to close this app ?")
        alertDialogBuilder.setIcon(R.drawable.exit)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            "Yes") { _, _ ->
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
