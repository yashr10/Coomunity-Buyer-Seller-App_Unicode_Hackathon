package com.example.unicodeinternalhackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Buyer_orders : AppCompatActivity() {

    //variable for recycler view
    private lateinit var rv:RecyclerView

    //variables for firestore
    val db = Firebase.firestore
    val mAuth = Firebase.auth

    //variable for data in recyclerView
    private lateinit var data:ArrayList<data_orders>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_orders)

        data = arrayListOf()

//        recyclerview for order list
        rv = findViewById(R.id.rv_buyer_orders)
        rv.apply {
            layoutManager = LinearLayoutManager(this@Buyer_orders)
            adapter = Adapter_Buyer_Orders(data,this@Buyer_orders)
        }

        //getting data from firebase and displaying it in
        db.collection("buyer")
            .document(mAuth.currentUser!!.uid)
            .collection("orders")
            .get()
            .addOnSuccessListener { orders ->
                for(i in orders)
                {
                    data.add(i.toObject(data_orders::class.java))
                }
                rv.adapter!!.notifyDataSetChanged()
            }

//        val intent = Intent()
//        intent.extras!!.getString("name")
//        intent.extras!!.getString("quantity")
//        intent.extras!!.getString("ppItem")
//        intent.extras!!.getString("img")



    }
}