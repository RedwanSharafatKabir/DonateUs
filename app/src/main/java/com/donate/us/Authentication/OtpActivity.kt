package com.donate.us.Authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.donate.us.R
import com.donate.us.databinding.ActivityLoginBinding
import com.donate.us.databinding.ActivityOtpBinding

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.code1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty()){
                    binding.code2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.code2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty()){
                    binding.code3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.code3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty()){
                    binding.code4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.code4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty()){
                    binding.code5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.code5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString().trim().isNotEmpty()){
                    binding.code6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
