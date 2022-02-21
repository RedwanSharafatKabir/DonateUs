package com.donate.us.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.donate.us.R
import com.donate.us.modelclasses.StoreDonationInfo
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class HistoryListAdapter(
    private var context: Context,
    private var storeDonationInfoArrayList:  ArrayList<StoreDonationInfo>
): RecyclerView.Adapter<HistoryListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.history_list_adapter, parent, false)

        return MyViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storeDonationInfo: StoreDonationInfo = storeDonationInfoArrayList[position]

        var foods = ""
        val chicken: String = storeDonationInfo.chicken.toString()
        val egg: String = storeDonationInfo.egg.toString()
        val redMeat: String = storeDonationInfo.redMeat.toString()
        val rice: String = storeDonationInfo.rice.toString()
        val vegetable: String = storeDonationInfo.vegetable.toString()
        val name: String = storeDonationInfo.userName.toString()
        val postStatus: String = storeDonationInfo.status.toString()
        val date: String = storeDonationInfo.date.toString()
        val time: String = storeDonationInfo.time.toString()

        if(chicken.isNotEmpty()){
            foods = foods + chicken + ", "
        }
        if(egg.isNotEmpty()){
            foods = foods + egg + ", "
        }
        if(redMeat.isNotEmpty()){
            foods = foods + redMeat + ", "
        }
        if(rice.isNotEmpty()){
            foods = foods + rice + ", "
        }
        if(vegetable.isNotEmpty()){
            foods = foods + vegetable + ", "
        }

        holder.name.text = name
        holder.food.text = foods
        holder.date.text = "$date\n$time"
        holder.status.text = postStatus
    }

    override fun getItemCount(): Int {
        return storeDonationInfoArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var food: TextView
        var date: TextView
        var status: TextView

        init {
            name = itemView.findViewById(R.id.donorName)
            food = itemView.findViewById(R.id.donorFood)
            date = itemView.findViewById(R.id.donorDate)
            status = itemView.findViewById(R.id.donorStatus)
        }
    }
}
