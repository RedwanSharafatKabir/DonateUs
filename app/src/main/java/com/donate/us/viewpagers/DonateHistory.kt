package com.donate.us.viewpagers

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donate.us.adapters.HistoryListAdapter
import com.donate.us.databinding.FragmentDonateHistoryBinding
import com.donate.us.internetcheck.CheckAvailableInternet
import com.donate.us.modelclasses.StoreDonationInfo
import com.donate.us.offlinedb.SharedPref
import com.google.firebase.database.*

class DonateHistory : Fragment() {

    private lateinit var binding: FragmentDonateHistoryBinding
    private lateinit var sharedPref: SharedPref
    private val checkAvailableInternet: CheckAvailableInternet = CheckAvailableInternet()
    private lateinit var userPhone: String
    private lateinit var userType: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyListAdapter: HistoryListAdapter
    private lateinit var donationArrayList: ArrayList<StoreDonationInfo>
    private var recyclerViewState: Parcelable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDonateHistoryBinding.inflate(layoutInflater)

        sharedPref = SharedPref()
        sharedPref.init(requireContext())
        userPhone = sharedPref.read("phoneKey", "").toString()
        userType = sharedPref.read("userTypeKey", "").toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Collected Foods")

        recyclerView = binding.historyList
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        donationArrayList = arrayListOf<StoreDonationInfo>()

        historyListAdapter = HistoryListAdapter(requireContext(), donationArrayList)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerViewState = recyclerView.layoutManager!!.onSaveInstanceState()!!
            }
        })

        if(checkAvailableInternet.checkInternet(requireContext())){
            getPaidDonations()

        } else {
            Toast.makeText(activity, "Turn on internet", Toast.LENGTH_SHORT).show()
            binding.historyProgress.visibility = View.INVISIBLE
        }

        return binding.root
    }

    private fun getPaidDonations() {
        if(userType.equals("Donor")){
            try {
                databaseReference.child(userPhone).addValueEventListener(object :
                    ValueEventListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            donationArrayList.clear()

                            for (items in snapshot.children) {
                                Log.i("items", items.value.toString())

                                val storeDonationInfo = items.getValue(StoreDonationInfo::class.java)!!
                                donationArrayList.add(storeDonationInfo)
                            }

                            recyclerView.adapter = historyListAdapter
                            historyListAdapter.notifyDataSetChanged()
                            recyclerView.layoutManager!!.onRestoreInstanceState(
                                recyclerViewState
                            )

                            binding.historyProgress.visibility = View.INVISIBLE

                        } catch (e: Exception) {
                            binding.historyProgress.visibility = View.INVISIBLE
                            Toast.makeText(activity, "Database error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        binding.historyProgress.visibility = View.INVISIBLE
                        Log.i("Error", error.message)
                    }
                })

            } catch (e: Exception) {
                Log.i("Exception", e.message.toString())
                binding.historyProgress.visibility = View.INVISIBLE
            }
        }

        if(userType.equals("Admin")){
            try {
                databaseReference.addValueEventListener(object: ValueEventListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            donationArrayList.clear()

                            for (items in snapshot.children) {
                                for (item in items.children) {
                                    Log.i("items_admin", item.value.toString())

                                    val storeDonationInfo = item.getValue(StoreDonationInfo::class.java)!!
                                    donationArrayList.add(storeDonationInfo)
                                }
                            }

                            recyclerView.adapter = historyListAdapter
                            historyListAdapter.notifyDataSetChanged()
                            recyclerView.layoutManager!!.onRestoreInstanceState(recyclerViewState)

                            binding.historyProgress.visibility = View.INVISIBLE

                        } catch (e: Exception){
                            binding.historyProgress.visibility = View.INVISIBLE
                            Toast.makeText(activity, "Database error", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        binding.historyProgress.visibility = View.INVISIBLE
                        Log.i("Error", error.message)
                    }
                })

            } catch (e: Exception){
                Log.i("Exception", e.message.toString())
                binding.historyProgress.visibility = View.INVISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(checkAvailableInternet.checkInternet(requireContext())){
            getPaidDonations()

        } else {
            Toast.makeText(activity, "Turn on internet", Toast.LENGTH_SHORT).show()
            binding.historyProgress.visibility = View.INVISIBLE
        }
    }
}
