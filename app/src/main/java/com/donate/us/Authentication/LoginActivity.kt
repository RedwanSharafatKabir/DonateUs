package com.donate.us.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.donate.us.R
import com.donate.us.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var phone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginProgressId.visibility = View.INVISIBLE
        val getUserPhone: EditText = binding.enterUserPhone

        binding.buttonConfirm.setOnClickListener(View.OnClickListener {
            binding.loginProgressId.visibility = View.VISIBLE
            phone = getUserPhone.text.toString()

            if(phone.isEmpty()){
                getUserPhone.setError("Enter Phone Number")
                binding.loginProgressId.visibility = View.INVISIBLE
            }

            if(phone.length<11){
                getUserPhone.setError("Invalid Phone Number")
                binding.loginProgressId.visibility = View.INVISIBLE
            }

            else {
                binding.loginProgressId.visibility = View.INVISIBLE

                val intent = Intent(this@LoginActivity, OtpActivity::class.java)
                startActivity(intent)
            }
        })

        binding.signUpPage.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        })
    }
}
