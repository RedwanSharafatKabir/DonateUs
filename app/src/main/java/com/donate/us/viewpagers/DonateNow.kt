package com.donate.us.viewpagers

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.donate.us.databinding.FragmentDonateNowBinding
import com.donate.us.internetcheck.CheckAvailableInternet
import com.donate.us.modelclasses.StoreDonationInfo
import com.donate.us.offlinedb.SharedPref
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updatePadding

class DonateNow : Fragment() {

    private lateinit var sharedPref: SharedPref
    private val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private lateinit var binding: FragmentDonateNowBinding
    private lateinit var expandLayout: LinearLayout
    private lateinit var profileImageUrl: String
    private lateinit var userPhone: String
    private lateinit var foodAddress: String
    private lateinit var peopleQuantity: String
    private lateinit var rice: String
    private lateinit var egg: String
    private lateinit var vegetable: String
    private lateinit var redMeat: String
    private lateinit var chicken: String
    private var foodName = "donated_food"
    private var uriProfileImage: Uri? = null
    private var countPeople = 1
    private lateinit var storageReference: StorageReference
    private lateinit var donationReference: DatabaseReference
    private lateinit var userReference: DatabaseReference
    private lateinit var progressDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDonateNowBinding.inflate(layoutInflater)

        sharedPref = SharedPref()
        sharedPref.init(requireActivity())
        userPhone = sharedPref.read("phoneKey", "").toString()

        userReference = FirebaseDatabase.getInstance().getReference("User Info")
        donationReference = FirebaseDatabase.getInstance().getReference("Donation Info")

        expandLayout = binding.foodTypesExpandView
        expandLayout.visibility = View.INVISIBLE
        binding.shrinkView.visibility = View.INVISIBLE
        binding.imageName.visibility = View.GONE

        binding.viewFoodTypes.setOnClickListener {
            val viewVisible = expandLayout.isVisible

            if(!viewVisible){
                expandLayout.visibility = View.VISIBLE
                binding.shrinkView.visibility = View.VISIBLE
                binding.expandView.visibility = View.INVISIBLE
                binding.quantityCard.visibility = View.INVISIBLE
                binding.imageUploadCard.visibility = View.INVISIBLE
            }

            if(viewVisible){
                expandLayout.visibility = View.INVISIBLE
                binding.shrinkView.visibility = View.INVISIBLE
                binding.expandView.visibility = View.VISIBLE
                binding.quantityCard.visibility = View.VISIBLE
                binding.imageUploadCard.visibility = View.VISIBLE
            }
        }

        binding.chooseImageFile.setOnClickListener {
            someActivityResultLauncher.launch("image/*")
        }

        binding.plus.setOnClickListener {
            countPeople++
            binding.peopleCountNumber.text = countPeople.toString()
        }

        binding.minus.setOnClickListener {
            if (countPeople > 1) {
                countPeople--
                binding.peopleCountNumber.text = countPeople.toString()
            }
        }

        binding.post.setOnClickListener {
            foodAddress = binding.enterAddress.text.toString()
            peopleQuantity = binding.peopleCountNumber.text.toString()

            if(binding.rice.isChecked){ rice = "Rice" } else if(!binding.rice.isChecked){ rice = "" }
            if(binding.egg.isChecked){ egg = "Egg" } else if(!binding.egg.isChecked){ egg = "" }
            if(binding.vegetable.isChecked){ vegetable = "Vegetable" } else if(!binding.vegetable.isChecked){ vegetable = "" }
            if(binding.chicken.isChecked){ chicken = "Chicken" } else if(!binding.chicken.isChecked){ chicken = "" }
            if(binding.redMeat.isChecked){ redMeat = "Red Meat" } else if(!binding.redMeat.isChecked){ redMeat = "" }

            if(rice.isEmpty() && egg.isEmpty() && vegetable.isEmpty() && chicken.isEmpty() && redMeat.isEmpty()){
                Toast.makeText(activity, "Select Food Types", Toast.LENGTH_SHORT).show()
            }

            if(foodAddress.isEmpty()){
                binding.enterAddress.error = "Enter pick address of food"
            }

            else if(binding.imageName.text.toString().isNotEmpty()) {
                if(checkAvailableInternet.checkInternet(requireActivity())){
                    uploadImageToFirebase(rice, egg, vegetable, chicken, redMeat, foodAddress, peopleQuantity, uriProfileImage!!)

                } else {
                    Toast.makeText(activity, "Turn on Internet", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(activity, "Upload an image of food", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private var someActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { result ->
        if (result != null) {
            uriProfileImage = result

            if(uriProfileImage!=null){
                binding.imageName.visibility = View.VISIBLE
                binding.imageName.text = "$foodName$userPhone.jpg"
            }
        }
    }

    private fun uploadImageToFirebase(rice: String, egg: String, vegetable: String, chicken: String,
                                      redMeat: String, foodAddress: String, peopleQuantity: String, uriProfileImage: Uri) {
        getDialogProgressBar()
        progressDialog = getDialogProgressBar().create()
        progressDialog.show()

        storageReference = FirebaseStorage.getInstance().getReference("food images/${System.currentTimeMillis()}$foodName.jpg")

        if (uriProfileImage != null) {
            storageReference.putFile(uriProfileImage).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    profileImageUrl = uri.toString()

                    saveUserInfo(rice, egg, vegetable, chicken, redMeat, foodAddress, peopleQuantity, profileImageUrl)

                }.addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener {
                Toast.makeText(activity, "Upload Failed", Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveUserInfo(rice: String, egg: String, vegetable: String, chicken: String,
                             redMeat: String, foodAddress: String, peopleQuantity: String, profileImageUrl: String) {
        var userName: String
        val df = SimpleDateFormat("dd/M/yyyy")
        val tf = SimpleDateFormat("hh:mm:ss")
        val currentDate = df.format(Date())
        val currentTime = tf.format(Date())

        userReference.child(userPhone).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userName = snapshot.child("userName").value.toString()

                val storeDonationInfo = StoreDonationInfo(userName, userPhone, rice, egg, vegetable, chicken, redMeat, peopleQuantity,
                    profileImageUrl, foodAddress, currentDate, currentTime, "unpaid")

                donationReference.child(userPhone).child(currentTime.toString()).setValue(storeDonationInfo)
                progressDialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", error.message)
                progressDialog.dismiss()
            }
        })

        progressDialog.dismiss()
        Toast.makeText(activity, "Successfully Uploaded", Toast.LENGTH_SHORT).show()

        binding.enterAddress.setText("")
        binding.imageName.visibility = View.GONE
        binding.peopleCountNumber.text = "1"
        binding.rice.isChecked = false
        binding.egg.isChecked = false
        binding.vegetable.isChecked = false
        binding.redMeat.isChecked = false
        binding.chicken.isChecked = false
    }

    private fun getDialogProgressBar(): AlertDialog.Builder {
        builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Uploading...")

        val progressBar = ProgressBar(requireContext())
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        progressBar.layoutParams = lp
        progressBar.updatePadding(bottom = 50)

        builder.setView(progressBar)

        return builder
    }
}
