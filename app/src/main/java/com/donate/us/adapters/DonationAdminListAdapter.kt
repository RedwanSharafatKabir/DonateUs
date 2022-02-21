package com.donate.us.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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

class DonationAdminListAdapter(
    private var context: Context,
    private var storeDonationInfoArrayList:  ArrayList<StoreDonationInfo>
): RecyclerView.Adapter<DonationAdminListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.donation_admin_list_adapter, parent, false)

        return MyViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val databaseReference: DatabaseReference
        val donorDatabaseReference: DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("Collected Foods")
        donorDatabaseReference = FirebaseDatabase.getInstance().getReference("Donation Info")
        val storeDonationInfo: StoreDonationInfo = storeDonationInfoArrayList[position]

        var foods = ""
        val chicken: String = storeDonationInfo.chicken.toString()
        val egg: String = storeDonationInfo.egg.toString()
        val redMeat: String = storeDonationInfo.redMeat.toString()
        val rice: String = storeDonationInfo.rice.toString()
        val vegetable: String = storeDonationInfo.vegetable.toString()
        val name: String = storeDonationInfo.userName.toString()
        val phone: String = storeDonationInfo.userPhone.toString()
        val peopleQuantity: String = storeDonationInfo.quantityPeople.toString()
        val pickAddress: String = storeDonationInfo.pickAdress.toString()
        val postStatus: String = storeDonationInfo.status.toString()
        val imageUrl: String = storeDonationInfo.avatar.toString()
        val date: String = storeDonationInfo.date.toString()
        val time: String = storeDonationInfo.time.toString()

        if(postStatus.equals("unpaid")){
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
            holder.people.text = peopleQuantity
            holder.pickLocation.text = pickAddress
            Picasso.get().load(imageUrl).into(holder.image)
        }

        holder.collect.setOnClickListener {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Confirm collection")
            alertDialogBuilder.setMessage("Collect food from donor now ?")
            alertDialogBuilder.setIcon(R.drawable.ic_baseline_fastfood_24)
            alertDialogBuilder.setCancelable(false)

            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                val storeDonationInfo = StoreDonationInfo(name, phone, rice, egg, vegetable, chicken, redMeat, peopleQuantity,
                    imageUrl, pickAddress, date, time, "paid")

                databaseReference.child(phone).child(time).setValue(storeDonationInfo)

                try{
                    donorDatabaseReference.child(phone).child(time).removeValue()

                } catch (e: Exception){
                    Log.i("Error", e.message.toString())
                }
            }

            alertDialogBuilder.setNeutralButton("No") {
                    dialog, _ -> dialog.cancel()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return storeDonationInfoArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var food: TextView
        var people: TextView
        var pickLocation: TextView
        var image: ImageView
        var collect: TextView

        init {
            name = itemView.findViewById(R.id.userName)
            food = itemView.findViewById(R.id.food)
            people = itemView.findViewById(R.id.peopleQuantity)
            pickLocation = itemView.findViewById(R.id.pickLocation)
            image = itemView.findViewById(R.id.foodImage)
            collect = itemView.findViewById(R.id.collectNow)
        }
    }
}
