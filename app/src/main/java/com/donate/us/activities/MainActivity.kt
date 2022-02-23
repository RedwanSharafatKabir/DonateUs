package com.donate.us.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.donate.us.R
import com.donate.us.adapters.AdminViewPagerAdapter
import com.donate.us.adapters.ViewPagerAdapter
import com.donate.us.databinding.ActivityMainBinding
import com.donate.us.offlinedb.SharedPref
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPref
    private lateinit var userType: String
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = SharedPref()
        sharedPref.init(applicationContext)
        userType = sharedPref.read("userTypeKey", "").toString()

        if(userType.equals("Donor")){
            binding.viewPager2.visibility = View.VISIBLE
            binding.tabLayout.visibility = View.VISIBLE
            binding.adminViewPager2.visibility = View.GONE
            binding.adminTabLayout.visibility = View.GONE

            viewPager = binding.viewPager2
            tabLayout = binding.tabLayout

            viewPager.adapter = ViewPagerAdapter(this)

            TabLayoutMediator(tabLayout, viewPager){tab, position ->
                when(position){
                    0 -> {
                        tab.text = resources.getText(R.string.home)
                    }

                    1 -> {
                        tab.text = resources.getText(R.string.donateNow)
                    }

                    2 -> {
                        tab.text = resources.getText(R.string.donateHistory)
                    }
                }
            }.attach()
        }

        if(userType.equals("Admin")){
            binding.viewPager2.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
            binding.adminViewPager2.visibility = View.VISIBLE
            binding.adminTabLayout.visibility = View.VISIBLE

            viewPager = binding.adminViewPager2
            tabLayout = binding.adminTabLayout

            viewPager.adapter = AdminViewPagerAdapter(this)

            TabLayoutMediator(tabLayout, viewPager){tab, position ->
                when(position){
                    0 -> {
                        tab.text = resources.getText(R.string.home)
                    }

                    1 -> {
                        tab.text = resources.getText(R.string.donateHistory)
                    }
                }
            }.attach()
        }

        toolbar = binding.toolBarId
        drawerLayout = binding.drawerId
        navigationView = binding.navigationViewId

        setSupportActionBar(toolbar)
        actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, toolbar,
            R.string.drawerOpen, R.string.drawerClose)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navigationView.itemIconTintList = null
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this@MainActivity)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START, true)
        val id = item.itemId

        when (id) {
            R.id.profileId -> {
                val intent = Intent(this@MainActivity, Profile::class.java)
                startActivity(intent)
            }

            R.id.aboutId -> {
                val intent = Intent(this@MainActivity, About::class.java)
                startActivity(intent)
            }
        }

        return true
    }
}
