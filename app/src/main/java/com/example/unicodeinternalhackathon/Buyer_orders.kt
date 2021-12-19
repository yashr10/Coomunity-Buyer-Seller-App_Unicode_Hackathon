package com.example.unicodeinternalhackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Buyer_orders : AppCompatActivity() {

    //variable for recycler view
    private lateinit var rv:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_orders)

        rv = findViewById(R.id.rv_buyer_orders)

        val intent = Intent()
        intent.extras!!.getString("name")
        intent.extras!!.getString("quantity")
        intent.extras!!.getString("ppItem")
        intent.extras!!.getString("img")

        rv.apply {
            layoutManager = LinearLayoutManager(this@Buyer_orders)
        }

    }
}