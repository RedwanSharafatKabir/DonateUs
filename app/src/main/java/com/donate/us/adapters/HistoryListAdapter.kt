package com.donate.us.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.donate.us.R
import com.donate.us.activities.ParticularPost
import com.donate.us.modelclasses.StoreDonationInfo
import com.donate.us.offlinedb.SharedPref
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
        val name: String = storeDonationInfo.userName.toString()
        val phone: String = storeDonationInfo.userPhone.toString()
        val chicken: String = storeDonationInfo.chicken.toString()
        val egg: String = storeDonationInfo.egg.toString()
        val redMeat: String = storeDonationInfo.redMeat.toString()
        val rice: String = storeDonationInfo.rice.toString()
        val vegetable: String = storeDonationInfo.vegetable.toString()
        val peopleQuantity: String = storeDonationInfo.quantityPeople.toString()
        val pickAddress: String = storeDonationInfo.pickAdress.toString()
        val postStatus: String = storeDonationInfo.status.toString()
        val imageUrl: String = storeDonationInfo.avatar.toString()
        val date: String = storeDonationInfo.date.toString()
        val time: String = storeDonationInfo.time.toString()

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

        holder.name.text = "Donor: $name"
        holder.food.text = "Foods: $foods"
        Picasso.get().load(imageUrl).into(holder.image)

        holder.viewPost.setOnClickListener{
            val intent = Intent(context, ParticularPost::class.java)
            intent.putExtra("donorName", name)
            intent.putExtra("donorPhone", phone)
            intent.putExtra("egg", egg)
            intent.putExtra("vegetable", vegetable)
            intent.putExtra("chicken", chicken)
            intent.putExtra("redMeat", redMeat)
            intent.putExtra("rice", rice)
            intent.putExtra("peopleQuantity", peopleQuantity)
            intent.putExtra("pickAddress", pickAddress)
            intent.putExtra("postStatus", postStatus)
            intent.putExtra("date", date)
            intent.putExtra("time", time)
            intent.putExtra("imageUrl", imageUrl)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return storeDonationInfoArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var food: TextView
        var image: ImageView
        var viewPost: ImageView

        init {
            name = itemView.findViewById(R.id.donorHistoryName)
            food = itemView.findViewById(R.id.foodHistory)
            image = itemView.findViewById(R.id.foodsHistoryImage)
            viewPost = itemView.findViewById(R.id.viewHistoryPost)
        }
    }
}
