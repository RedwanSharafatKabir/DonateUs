package com.donate.us.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.donate.us.databinding.ActivityMainBinding
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.donate.us.adapters.ViewPagerAdapter
import com.donate.us.R
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewPager = binding.viewPager2
        tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            when(position){
                0 -> {
                    tab.text = resources.getText(R.string.home)
                    tab.icon = resources.getDrawable(R.drawable.ic_baseline_home_24, theme)
                }

                1 -> {
                    tab.text = resources.getText(R.string.donateNow)
                    tab.icon = resources.getDrawable(R.drawable.ic_baseline_card_giftcard_24, theme)
                }

                2 -> {
                    tab.text = resources.getText(R.string.donateHistory)
                    tab.icon = resources.getDrawable(R.drawable.ic_baseline_history_24, theme)
                }
            }
        }.attach()

        binding.profilePage.setOnClickListener {
            val intent = Intent(this@MainActivity, Profile::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
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
