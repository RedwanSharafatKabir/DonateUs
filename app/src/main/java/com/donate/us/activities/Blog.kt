package com.donate.us.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.donate.us.databinding.ActivityBlogBinding

class Blog : AppCompatActivity() {

    private lateinit var binding : ActivityBlogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backFromBlog.setOnClickListener{
            super.onBackPressed()
        }
    }

}
