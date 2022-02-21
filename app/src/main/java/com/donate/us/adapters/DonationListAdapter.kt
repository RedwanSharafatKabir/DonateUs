package com.donate.us.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.donate.us.R
import com.donate.us.modelclasses.StoreDonationInfo
import com.donate.us.offlinedb.SharedPref
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.lang.Exception

class DonationListAdapter(
    private var context: Context,
    private var storeDonationInfoArrayList:  ArrayList<StoreDonationInfo>
    ): RecyclerView.Adapter<DonationListAdapter.MyViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var sharedPref: SharedPref
    private lateinit var userPhone: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.donation_list_adapter, parent, false)

        return MyViewHolder(view)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sharedPref = SharedPref()
        sharedPref.init(context)
        userPhone = sharedPref.read("phoneKey", "").toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Donation Info")

        val storeDonationInfo: StoreDonationInfo = storeDonationInfoArrayList[position]

        var foods = ""
        val chicken: String = storeDonationInfo.chicken.toString()
        val egg: String = storeDonationInfo.egg.toString()
        val redMeat: String = storeDonationInfo.redMeat.toString()
        val rice: String = storeDonationInfo.rice.toString()
        val vegetable: String = storeDonationInfo.vegetable.toString()
        val name: String = storeDonationInfo.userName.toString()
        val peopleQuantity: String = storeDonationInfo.quantityPeople.toString()
        val pickAddress: String = storeDonationInfo.pickAdress.toString()
        val postStatus: String = storeDonationInfo.status.toString()
        val imageUrl: String = storeDonationInfo.avatar.toString()
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
        holder.people.text = peopleQuantity
        holder.pickLocation.text = pickAddress
        Picasso.get().load(imageUrl).into(holder.image)

        if(postStatus.equals("paid")){
            holder.status.text = "Collected"
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.green))
            holder.status.background = ContextCompat.getDrawable(context, R.drawable.green_border_rectangle)
            holder.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_complete,0)
        }

        holder.delete.setOnClickListener{
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("DELETE !")
            alertDialogBuilder.setMessage("Do you want to delete this post?")
            alertDialogBuilder.setCancelable(false)

            alertDialogBuilder.setPositiveButton(
                "Yes") { _, _ ->
                try {
                    databaseReference.child(userPhone).child(time).removeValue()
                    Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Log.i("Error_Db", e.message.toString())
                }
            }

            alertDialogBuilder.setNeutralButton(
                "No"
            ) { dialog, _ -> dialog.cancel() }

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
        var status: TextView
        var image: ImageView
        var delete: ImageView

        init {
            name = itemView.findViewById(R.id.userName)
            food = itemView.findViewById(R.id.food)
            people = itemView.findViewById(R.id.peopleQuantity)
            pickLocation = itemView.findViewById(R.id.pickLocation)
            status = itemView.findViewById(R.id.status)
            image = itemView.findViewById(R.id.foodImage)
            delete = itemView.findViewById(R.id.deleteBtn)
        }
    }
}
