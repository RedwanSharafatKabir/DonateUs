package com.donate.us.viewpagers

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.donate.us.databinding.FragmentHomePageBinding
import com.donate.us.internetcheck.CheckAvailableInternet
import com.donate.us.offlinedb.SharedPref
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Parcelable
import android.util.Log
import com.donate.us.adapters.DonationListAdapter
import com.donate.us.modelclasses.StoreDonationInfo
import com.google.firebase.database.*
import android.os.Looper




class HomePage : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var sharedPref: SharedPref
    private val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private lateinit var userPhone: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var donationListAdapter: DonationListAdapter
    private lateinit var donationArrayList: ArrayList<StoreDonationInfo>
    private var recyclerViewState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomePageBinding.inflate(layoutInflater)

        sharedPref = SharedPref()
        sharedPref.init(requireContext())
        userPhone = sharedPref.read("phoneKey", "").toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Donation Info")

        recyclerView = binding.donationList
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        donationArrayList = arrayListOf<StoreDonationInfo>()

        donationListAdapter = DonationListAdapter(requireContext(), donationArrayList)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerViewState = recyclerView.layoutManager!!.onSaveInstanceState()!!
            }
        })

        if(checkAvailableInternet.checkInternet(requireContext())){
            getUnpaidDonations()

        } else {
            Toast.makeText(activity, "Turn on internet", Toast.LENGTH_SHORT).show()
            binding.homeProgress.visibility = View.INVISIBLE
        }

        return binding.root
    }

    private fun getUnpaidDonations() {
        binding.noPost.visibility = View.VISIBLE

        try {
            databaseReference.child(userPhone).addValueEventListener(object: ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        donationArrayList.clear()

                        for (items in snapshot.children) {
                            Log.i("items", items.value.toString())

                            val storeDonationInfo = items.getValue(StoreDonationInfo::class.java)!!
                            donationArrayList.add(storeDonationInfo)
                        }

                        recyclerView.adapter = donationListAdapter
                        donationListAdapter.notifyDataSetChanged()
                        recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)

                        binding.homeProgress.visibility = View.INVISIBLE
                        binding.noPost.visibility = View.INVISIBLE

                    } catch (e: Exception){
                        binding.homeProgress.visibility = View.INVISIBLE
                        Toast.makeText(activity, "Database error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.homeProgress.visibility = View.INVISIBLE
                    Log.i("Error", error.message)
                }
            })

        } catch (e: Exception){
            Log.i("Exception", e.message.toString())
            binding.homeProgress.visibility = View.INVISIBLE
        }

        refresh(1000)
    }

    private fun refresh(milliSecond: Int) {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable { getUnpaidDonations() }

        handler.postDelayed(runnable, milliSecond.toLong())
    }

    override fun onResume() {
        super.onResume()

        if(checkAvailableInternet.checkInternet(requireContext())){
            getUnpaidDonations()

        } else {
            Toast.makeText(activity, "Turn on internet", Toast.LENGTH_SHORT).show()
            binding.homeProgress.visibility = View.INVISIBLE
        }
    }
}
