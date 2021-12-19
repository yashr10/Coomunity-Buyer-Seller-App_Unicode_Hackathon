package com.example.unicodeinternalhackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SellerOrders : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_orders)




    }
}

class SellerOrdersAdapter() : RecyclerView.Adapter<SellerOrdersAdapter.ViewHolder>(){

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val image : ImageView = v.findViewById<ImageView>(R.id.iv_image)
        val name: TextView = v.findViewById<TextView>(R.id.tv_name)
        val amount: TextView= v.findViewById<TextView>(R.id.tv_amount)
        val quantity: TextView= v.findViewById<TextView>(R.id.tv_quantity)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val v = inflater.inflate(R.layout.card_activity_seller_order,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}