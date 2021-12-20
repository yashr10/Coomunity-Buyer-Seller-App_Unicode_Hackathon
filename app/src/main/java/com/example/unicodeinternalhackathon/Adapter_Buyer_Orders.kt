package com.example.unicodeinternalhackathon

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Adapter_Buyer_Orders(val data:ArrayList<data_orders>, val context: Context):RecyclerView.Adapter<Adapter_Buyer_Orders.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_view_buyer_order,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val input = data[position]
        holder.bind(input,context)


        holder.itemView.setOnClickListener {
            val intent = Intent(context,Buyer_OrderDescription::class.java)
            intent.putExtra("order",data[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(v: View):RecyclerView.ViewHolder(v)
    {
        val mAuth = Firebase.auth
        val db = Firebase.firestore
        var status :TextView = v.findViewById(R.id.buyer_order_status)
        val tvName :TextView = v.findViewById(R.id.tv_buyer_order_name)
        val tvQuant :TextView = v.findViewById(R.id.tv_buyer_order_quantity)
        val tvAmt :TextView = v.findViewById(R.id.tv_buyer_order_amount)
        val img:ImageView = v.findViewById(R.id.im_buyer_order_img)
        fun bind(data:data_orders,context: Context)
        {
            tvName.text = data.Name
            tvQuant.text = data.Quantity
            tvAmt.text = data.TotalAmount
            Glide.with(context)
                .load(data.Image.toString())
                .into(img)
            db.collection("seller")
                .document(data.SellerId)
                .collection("orders")
                .document(data.ProductId)
                .get()
                .addOnSuccessListener {
                    if(it["Status"].toString() == "2")
                    {
                        status.text = "Rejected"
                        status.setTextColor(ContextCompat.getColor(context, R.color.design_default_color_error))
                    }
                    else if(it["Status"].toString() == "1")
                    {
                        status.text = "Confirmed"
                        status.setTextColor(Color.parseColor("#2CDC06"));
                    }
                    else
                    {
                        status.text = "Pending"
                        status.setTextColor(Color.parseColor("#302A2A"))

                    }
                }
        }
    }
}