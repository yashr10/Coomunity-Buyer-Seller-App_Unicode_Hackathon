package com.example.unicodeinternalhackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Buyer_orders : AppCompatActivity() {

    //variables for left nav of buyer
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nav: NavigationView

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

        // assigning of navigation view,
        // header variable for header of navigation
        //userName variable to access username in header
        nav = findViewById(R.id.buyer_nav)
        val header = nav.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.tv_left_nav_name)
//        userName.text = mAuth.currentUser!!.uid


        //assigning toolbar and drawer to work simultaneously
        toolbar = findViewById(R.id.buyer_toolbar)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer = findViewById(R.id.buyer_left_nav)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

//        recyclerview for order list
        rv = findViewById(R.id.rv_buyer_orders)
        rv.apply {
            layoutManager = LinearLayoutManager(this@Buyer_orders)
            adapter = Adapter_Buyer_Orders(data,this@Buyer_orders)
        }

        //getting data from firebase and displaying it in
        db.collection("buyer")
            .document("ABi3Se4zp4YH3IjGMOWm5Xc9kFl2")
            .collection("orders")
            .get()
            .addOnSuccessListener { orders ->
                for(i in orders)
                {
                    data.add(i.toObject(data_orders::class.java))
                }
                rv.adapter!!.notifyDataSetChanged()
            }


    }
}