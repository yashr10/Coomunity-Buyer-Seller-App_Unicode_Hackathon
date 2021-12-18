package com.example.unicodeinternalhackathon

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter_All_Products(var data: ArrayList<data_all_products>, private val context:Context) :
    RecyclerView.Adapter<Adapter_All_Products.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_view_all_products, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val input = data[position]

        //function for showing data in recyclerView fields
        holder.bind(input,context)

        //on click of recycler sends data to new activity and open new activity
        holder.itemView.setOnClickListener {
            val intent = Intent(context,All_Product_Desc::class.java)
            intent.putExtra("name",data[position].Name)
            intent.putExtra("img",data[position].Image)
            intent.putExtra("desc",data[position].Description)
            intent.putExtra("dp",data[position].DiscountedPrice)
            intent.putExtra("min",data[position].MinQuantity)
            intent.putExtra("mrp",data[position].MRP)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        //variables for recyclerview fields
        private var tvName: TextView = v.findViewById(R.id.tv_all_products_name)
        private var tvMrp: TextView = v.findViewById(R.id.tv_all_products_mrp)
        private var tvDp: TextView = v.findViewById(R.id.tv_all_products_dp)
        var img: ImageView = v.findViewById(R.id.im_all_products_img)

        // function that assigns data to recyclerview fields
        fun bind(data: data_all_products,context: Context) {
            tvName.text = data.Name
            tvDp.text = data.DiscountedPrice
            tvMrp.text = data.MRP
            Glide.with(context)
                .load(data.Image)
                .into(img)
        }
    }
}