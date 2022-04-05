package com.donate.us.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.donate.us.R
import com.donate.us.databinding.ActivityParticularPostBinding
import com.donate.us.internetcheck.CheckAvailableInternet
import com.donate.us.modelclasses.StoreDonationInfo
import com.donate.us.offlinedb.SharedPref
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ParticularPost : AppCompatActivity() {

    private lateinit var binding: ActivityParticularPostBinding
    private lateinit var sharedPref: SharedPref
    private val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private lateinit var pendingFoodReference: DatabaseReference
    private lateinit var collectedFoodReference: DatabaseReference
    private lateinit var userPhone: String
    private lateinit var userType: String
    private lateinit var donorName: String
    private lateinit var donorPhone: String
    private lateinit var egg: String
    private lateinit var vegetable: String
    private lateinit var chicken: String
    private lateinit var redMeat: String
    private lateinit var rice: String
    private lateinit var peopleQuantity: String
    private lateinit var pickAddress: String
    private lateinit var postStatus: String
    private lateinit var date: String
    private lateinit var time: String
    private lateinit var imageUrl: String
    private var foods: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticularPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = SharedPref()
        sharedPref.init(applicationContext)
        userType = sharedPref.read("userTypeKey", "").toString()
        pendingFoodReference = FirebaseDatabase.getInstance().getReference("Donation Info")
        collectedFoodReference = FirebaseDatabase.getInstance().getReference("Collected Foods")

        val intent: Intent = intent
        imageUrl = intent.getStringExtra("imageUrl").toString()
        Picasso.get().load(imageUrl).into(binding.foodImage)
        donorName = intent.getStringExtra("donorName").toString()
        donorPhone = intent.getStringExtra("donorPhone").toString()
        egg = intent.getStringExtra("egg").toString()
        vegetable = intent.getStringExtra("vegetable").toString()
        chicken = intent.getStringExtra("chicken").toString()
        redMeat = intent.getStringExtra("redMeat").toString()
        rice = intent.getStringExtra("rice").toString()
        peopleQuantity = intent.getStringExtra("peopleQuantity").toString()
        pickAddress = intent.getStringExtra("pickAddress").toString()
        postStatus = intent.getStringExtra("postStatus").toString()
        date = intent.getStringExtra("date").toString()
        time = intent.getStringExtra("time").toString()

        if(chicken.isNotEmpty()){
            foods = "$foods$chicken, "
        }
        if(egg.isNotEmpty()){
            foods = "$foods$egg, "
        }
        if(redMeat.isNotEmpty()){
            foods = "$foods$redMeat, "
        }
        if(rice.isNotEmpty()){
            foods = "$foods$rice, "
        }
        if(vegetable.isNotEmpty()){
            foods = "$foods$vegetable"
        }

        binding.userName.text = donorName
        binding.donorPhone.text = "+88$donorPhone"
        binding.food.text = foods
        binding.peopleQuantity.text = peopleQuantity
        binding.pickLocation.text = pickAddress

        if(userType.equals("Admin") && postStatus.equals("unpaid")){
            binding.deleteBtn.visibility = View.GONE
            binding.collectNow.visibility = View.VISIBLE
            binding.callNow.visibility = View.VISIBLE
        }

        if(userType.equals("Admin") && postStatus.equals("paid")){
            binding.deleteBtn.visibility = View.GONE
            binding.collectNow.visibility = View.GONE
            binding.callNow.visibility = View.VISIBLE
            binding.status.text = "Collected"
            binding.status.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
            binding.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_complete, 0)
        }

        if(userType.equals("Donor") && postStatus.equals("unpaid")){
            binding.deleteBtn.visibility = View.VISIBLE
            binding.collectNow.visibility = View.GONE
            binding.callNow.visibility = View.GONE
        }

        if(userType.equals("Donor") && postStatus.equals("paid")){
            binding.deleteBtn.visibility = View.VISIBLE
            binding.collectNow.visibility = View.GONE
            binding.callNow.visibility = View.GONE
            binding.status.text = "Collected"
            binding.status.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))
            binding.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_complete, 0)
        }

        binding.backFromPost.setOnClickListener {
            super.onBackPressed()
        }

        binding.callNow.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$donorPhone")

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.CALL_PHONE), 1)

                startActivity(callIntent)

            } else {
                startActivity(callIntent)
            }
        }

        binding.deleteBtn.setOnClickListener{
            if(checkAvailableInternet.checkInternet(this)){
                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("DELETE !")
                alertDialogBuilder.setMessage("Do you want to delete this post?")
                alertDialogBuilder.setCancelable(false)

                alertDialogBuilder.setPositiveButton(
                    "Yes") { _, _ ->
                    try {
                        pendingFoodReference.child(donorPhone).child(time).removeValue()
                        Toast.makeText(applicationContext, "Post Deleted", Toast.LENGTH_SHORT).show()

                    } catch (e: java.lang.Exception) {
                        Log.i("Error_Db", e.message.toString())
                    }
                }

                alertDialogBuilder.setNeutralButton(
                    "No"
                ) { dialog, _ -> dialog.cancel() }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()

            } else {
                Toast.makeText(applicationContext, "Turn Wifi / Mobile Data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.collectNow.setOnClickListener {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirm collection")
            alertDialogBuilder.setMessage("Collect food from donor now ?")
            alertDialogBuilder.setIcon(R.drawable.ic_baseline_fastfood_24)
            alertDialogBuilder.setCancelable(false)

            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                val storeDonationInfo = StoreDonationInfo(
                    donorName, donorPhone, rice, egg, vegetable, chicken, redMeat, peopleQuantity,
                    imageUrl, pickAddress, date, time, "paid"
                )

                collectedFoodReference.child(donorPhone).child(time).setValue(storeDonationInfo)
                Toast.makeText(applicationContext, "Food Collected Successfully", Toast.LENGTH_SHORT).show()

                try {
                    pendingFoodReference.child(donorPhone).child(time).removeValue()

                } catch (e: Exception) {
                    Log.i("Error", e.message.toString())
                }
            }

            alertDialogBuilder.setNeutralButton("No") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
}
