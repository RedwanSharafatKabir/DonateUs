package com.donate.us.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.donate.us.databinding.ActivityMainBinding
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.donate.us.adapters.ViewPagerAdapter
import com.donate.us.R
import com.donate.us.adapters.AdminViewPagerAdapter
import com.donate.us.internetcheck.CheckAvailableInternet
import com.donate.us.offlinedb.SharedPref
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var imgDbReference: DatabaseReference
    private val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private lateinit var sharedPref: SharedPref
    private lateinit var userPhone: String
    private lateinit var userType: String
    private lateinit var profilePic: CircleImageView

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = SharedPref()
        sharedPref.init(applicationContext)
        userPhone = sharedPref.read("phoneKey", "").toString()
        userType = sharedPref.read("userTypeKey", "").toString()
        imgDbReference = FirebaseDatabase.getInstance().getReference("User Images")

        if(userType.equals("Donor")){
            binding.viewPager2.visibility = View.VISIBLE
            binding.tabLayout.visibility = View.VISIBLE
            binding.adminViewPager2.visibility = View.GONE
            binding.adminTabLayout.visibility = View.GONE

            profilePic = binding.profilePage
            viewPager = binding.viewPager2
            tabLayout = binding.tabLayout

            viewPager.adapter = ViewPagerAdapter(this)

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
        }

        if(userType.equals("Admin")){
            binding.viewPager2.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
            binding.adminViewPager2.visibility = View.VISIBLE
            binding.adminTabLayout.visibility = View.VISIBLE

            profilePic = binding.profilePage
            viewPager = binding.adminViewPager2
            tabLayout = binding.adminTabLayout

            viewPager.adapter = AdminViewPagerAdapter(this)

            TabLayoutMediator(tabLayout, viewPager){tab, position ->
                when(position){
                    0 -> {
                        tab.text = resources.getText(R.string.home)
                        tab.icon = resources.getDrawable(R.drawable.ic_baseline_home_24, theme)
                    }

                    1 -> {
                        tab.text = resources.getText(R.string.donateHistory)
                        tab.icon = resources.getDrawable(R.drawable.ic_baseline_card_giftcard_24, theme)
                    }
                }
            }.attach()
        }

        if (checkAvailableInternet.checkInternet(applicationContext)) {
            getProfileInfo(userPhone)
        } else {
            Toast.makeText(applicationContext, "Turn on internet", Toast.LENGTH_SHORT).show()
        }

        profilePic.setOnClickListener {
            val intent = Intent(this@MainActivity, Profile::class.java)
            startActivity(intent)
        }
    }

    private fun getProfileInfo(userPhone: String) {
        try {
            imgDbReference.child(userPhone).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val imageUrl = snapshot.child("avatar").value.toString()
                        Picasso.get().load(imageUrl).into(profilePic)

                    } catch (e: java.lang.Exception) {
                        Log.i("Error", e.message.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Error", error.message)
                }
            })
        } catch (e: java.lang.Exception) {
            Log.i("Error", e.message.toString())
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
