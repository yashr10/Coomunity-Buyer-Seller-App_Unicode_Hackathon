package com.example.unicodeinternalhackathon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adapter_Buyer_Order(val data:ArrayList<data_orders>,val context: Context):RecyclerView.Adapter<Adapter_Buyer_Order.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_view_buyer_order,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val input = data[position]
        holder.bind(input,context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(v: View):RecyclerView.ViewHolder(v)
    {
        fun bind(data:data_orders,context: Context)
        {

        }
    }
}