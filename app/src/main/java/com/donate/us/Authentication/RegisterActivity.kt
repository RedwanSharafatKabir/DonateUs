package com.donate.us.Authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.donate.us.R
import com.donate.us.databinding.ActivityOtpBinding
import com.donate.us.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
