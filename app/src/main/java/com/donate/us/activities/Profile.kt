package com.donate.us.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AlertDialog
import com.donate.us.authentication.LoginActivity
import com.donate.us.R
import com.donate.us.offlinedb.SharedPref
import com.donate.us.databinding.ActivityProfileBinding
import com.donate.us.internetcheck.CheckAvailableInternet
import com.donate.us.modelclasses.StoreUserData
import com.donate.us.modelclasses.StoreUserImageUrlData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPref: SharedPref
    private lateinit var auth: FirebaseAuth
    private val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private lateinit var userPhone: String
    private lateinit var address: String
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var userType: String
    private lateinit var profilePic: CircleImageView
    private lateinit var donorReference: DatabaseReference
    private lateinit var adminReference: DatabaseReference
    private lateinit var imgDbReference: DatabaseReference
    private lateinit var uriProfileImage: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var profileImageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = SharedPref()
        sharedPref.init(applicationContext)
        userPhone = sharedPref.read("phoneKey", "").toString()
        userType = sharedPref.read("userTypeKey", "").toString()

        auth = FirebaseAuth.getInstance()
        donorReference = FirebaseDatabase.getInstance().getReference("User Info")
        adminReference = FirebaseDatabase.getInstance().getReference("Admin Info")
        imgDbReference = FirebaseDatabase.getInstance().getReference("User Images")
        profilePic = binding.profileImage

        if (checkAvailableInternet.checkInternet(applicationContext)) {
            getProfileInfo(userPhone)
        }
        else {
            Toast.makeText(applicationContext, "Turn on internet", Toast.LENGTH_SHORT).show()
            binding.profileProgress.visibility = View.INVISIBLE
        }

        binding.backFromProfile.setOnClickListener {
            super.onBackPressed()
        }

        binding.profileLogOut.setOnClickListener {
            logoutApp()
        }

        binding.blog.setOnClickListener {
            val intent = Intent(this@Profile, Blog::class.java)
            startActivity(intent)
        }

        binding.saveAddress.setOnClickListener{
            val enterAddress = binding.profileAddress.text.toString()
            if(enterAddress.isEmpty()){
                binding.profileAddress.error = "Enter Address"

            } else if(enterAddress.isNotEmpty()){
                binding.profileProgress.visibility = View.VISIBLE
                saveAllInfo(name, email, userPhone, enterAddress)
            }
        }

        binding.aboutPage.setOnClickListener{
            val intent = Intent(this@Profile, About::class.java)
            startActivity(intent)
        }

        binding.profileChangeImage.setOnClickListener{
            someActivityResultLauncher.launch("image/*")
        }
    }

    private fun getProfileInfo(userPhone: String) {
        if(userType.equals("Donor")) {
            donorReference.child(userPhone).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        address = snapshot.child("userAddress").value.toString()
                        name = snapshot.child("userName").value.toString()
                        email = snapshot.child("userEmail").value.toString()

                        binding.profileName.text = name
                        binding.profileEmail.text = email
                        binding.profilePhone.text = userPhone

                        if (address != "notSaved") {
                            binding.profileAddress.setText(address)
                        }

                        // Get user image
                        try {
                            imgDbReference.child(userPhone)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        try {
                                            val imageUrl = snapshot.child("avatar").value.toString()
                                            Picasso.get().load(imageUrl).into(profilePic)
                                            binding.profileProgress.visibility = View.INVISIBLE

                                        } catch (e: java.lang.Exception) {
                                            binding.profileProgress.visibility = View.INVISIBLE
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        binding.profileProgress.visibility = View.INVISIBLE
                                        Log.i("Error", error.message)
                                    }
                                })
                        } catch (e: java.lang.Exception) {
                            binding.profileProgress.visibility = View.INVISIBLE
                            Log.i("Error", e.message.toString())
                        }

                    } catch (e: Exception) {
                        binding.profileProgress.visibility = View.INVISIBLE
                        Toast.makeText(
                            applicationContext,
                            "Authentication failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.profileProgress.visibility = View.INVISIBLE
                    Log.i("Error", error.message)
                }
            })
        }

        if(userType.equals("Admin")) {
            adminReference.child(userPhone).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        address = snapshot.child("userAddress").value.toString()
                        name = snapshot.child("userName").value.toString()
                        email = snapshot.child("userEmail").value.toString()

                        binding.profileName.text = name
                        binding.profileEmail.text = email
                        binding.profilePhone.text = userPhone

                        if (address != "notSaved") {
                            binding.profileAddress.setText(address)
                        }

                        // Get user image
                        try {
                            imgDbReference.child(userPhone)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        try {
                                            val imageUrl = snapshot.child("avatar").value.toString()
                                            Picasso.get().load(imageUrl).into(profilePic)
                                            binding.profileProgress.visibility = View.INVISIBLE

                                        } catch (e: java.lang.Exception) {
                                            binding.profileProgress.visibility = View.INVISIBLE
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        binding.profileProgress.visibility = View.INVISIBLE
                                        Log.i("Error", error.message)
                                    }
                                })
                        } catch (e: java.lang.Exception) {
                            binding.profileProgress.visibility = View.INVISIBLE
                            Log.i("Error", e.message.toString())
                        }

                    } catch (e: Exception) {
                        binding.profileProgress.visibility = View.INVISIBLE
                        Toast.makeText(
                            applicationContext,
                            "Authentication failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.profileProgress.visibility = View.INVISIBLE
                    Log.i("Error", error.message)
                }
            })
        }
    }

    private var someActivityResultLauncher = registerForActivityResult(
        GetContent()) { result ->
        if (result != null) {
            uriProfileImage = result

            profilePic.setImageURI(uriProfileImage)

            Picasso.get().load(uriProfileImage).into(profilePic)

            uploadImageToFirebase()
        }
    }

    private fun uploadImageToFirebase() {
        binding.profileProgress.visibility = View.VISIBLE

        storageReference = FirebaseStorage.getInstance().getReference("profile images/$userPhone.jpg")

        if (uriProfileImage != null) {
            storageReference.putFile(uriProfileImage).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    profileImageUrl = uri.toString()

                    saveUserInfo()

                }.addOnFailureListener { e ->
                    binding.profileProgress.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Upload Failed", Toast.LENGTH_LONG).show()
                binding.profileProgress.visibility = View.INVISIBLE
            }
        }
    }

    private fun saveUserInfo() {
        val user = auth.currentUser

        if (user != null && profileImageUrl != null) {
            val profile: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(profileImageUrl)).build()

            user.updateProfile(profile).addOnCompleteListener { }

            val storeUserImageUrlData = StoreUserImageUrlData(profileImageUrl)
            imgDbReference.child(userPhone).setValue(storeUserImageUrlData)

            binding.profileProgress.visibility = View.INVISIBLE
            Toast.makeText(applicationContext, "Successfully Uploaded", Toast.LENGTH_SHORT).show()

        } else {
            binding.profileProgress.visibility = View.INVISIBLE
        }
    }

    private fun saveAllInfo(name: String, email: String, userPhone: String, enterAddress: String) {
        if (checkAvailableInternet.checkInternet(applicationContext)) {
            if(userType.equals("Donor")) {
                val storeUserData = StoreUserData(name, email, userPhone, enterAddress)
                donorReference.child(userPhone).setValue(storeUserData)
            }

            if(userType.equals("Admin")) {
                val storeUserData = StoreUserData(name, email, userPhone, enterAddress)
                adminReference.child(userPhone).setValue(storeUserData)
            }

            Toast.makeText(this@Profile, "Address updated", Toast.LENGTH_SHORT).show()
            binding.profileProgress.visibility = View.INVISIBLE
        }

        else {
            Toast.makeText(applicationContext, "Turn on internet", Toast.LENGTH_SHORT).show()
            binding.profileProgress.visibility = View.INVISIBLE
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
