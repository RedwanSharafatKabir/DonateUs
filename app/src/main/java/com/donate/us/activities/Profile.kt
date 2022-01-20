package com.donate.us.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.donate.us.authentication.LoginActivity
import com.donate.us.R
import com.donate.us.offlinedb.SharedPref
import com.donate.us.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = SharedPref()
        sharedPref.init(applicationContext)

        binding.backFromProfile.setOnClickListener {
            super.onBackPressed()
        }

        binding.profileLogOut.setOnClickListener {
            logoutApp()
        }
    }

    private fun logoutApp() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@Profile)
        alertDialogBuilder.setTitle("LOGOUT !")
        alertDialogBuilder.setMessage("Do you want to logout from app ?")
        alertDialogBuilder.setIcon(R.drawable.exit)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton(
            "Yes") { _, _ ->
            sharedPref.write("phoneKey", "")

            finish()
            val intent = Intent(this@Profile, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        alertDialogBuilder.setNeutralButton(
            "No"
        ) { dialog, _ -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
