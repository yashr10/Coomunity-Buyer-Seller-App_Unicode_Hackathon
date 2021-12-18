package com.example.unicodeinternalhackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class All_Products : AppCompatActivity() {

    //variables for left nav of buyer
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nav: NavigationView

    //rv for recyclerview of all products
    private lateinit var rv: RecyclerView

    //data for adapter of all products
    private lateinit var data: ArrayList<data_all_products>

    //firebase variables
    val db = Firebase.firestore
    val mAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_products)
//        data = arrayListOf()
//
//        db.collection("seller")
//            .whereEqualTo("user_id", 1)
//            .get()
//            .addOnFailureListener {
//                setContentView(R.layout.buyer_all_products)
//                // assigning of navigation view,
//                // header variable for header of navigation
//                //userName variable to access username in header
//                nav = findViewById(R.id.buyer_nav)
//                val header = nav.getHeaderView(0)
//                val userName = header.findViewById<TextView>(R.id.tv_left_nav_name)
//                userName.text = mAuth.currentUser!!.uid
//
//                //assigning toolbar and drawer to work simultaneously
//                toolbar = findViewById(R.id.buyer_toolbar)
//                setSupportActionBar(toolbar)
//                drawer = findViewById(R.id.buyer_left_nav)
//                toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
//                drawer.addDrawerListener(toggle)
//                toggle.syncState()
//                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//
//
////                nav.setNavigationItemSelectedListener {
////                    when (it.itemId) {
////                        R.id.nav_buyer_all_products -> {
////
////                        }
////                        R.id.nav_buyer_orders -> {
////
////                        }
////                        R.id.nav_buyer_profile -> {
////
////                        }
////                    }
////                }
//            }
//            .addOnSuccessListener {
//                // variable of navigation view,
//                // header variable for header of navigation
//                //userName variable to access username in header
//                nav = findViewById(R.id.seller_nav)
//                val header = nav.getHeaderView(0)
//                val userName = header.findViewById<TextView>(R.id.tv_left_nav_name)
//
//                //assigning toolbar and drawer to work simultaneously
//                toolbar = findViewById(R.id.seller_toolbar)
//                setSupportActionBar(toolbar)
//                drawer = findViewById(R.id.seller_left_nav)
//                toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
//                drawer.addDrawerListener(toggle)
//                toggle.syncState()
//                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//
//
////                nav.setNavigationItemSelectedListener {
////                    when (it.itemId) {
////                        R.id.nav_seller_orders -> {
////
////                        }
////                        R.id.nav_seller_all_products -> {
////
////                        }
////                        R.id.nav_seller_products -> {
////
////                        }
////                        R.id.nav_seller_min_amount -> {
////
////                        }
////                        R.id.nav_seller_profile -> {
////
////                        }
////                    }
////                }
//            }
//
//        //assinging rv and adapter
//        rv = findViewById(R.id.rv_buyer_products)
//        rv.apply {
//            layoutManager = LinearLayoutManager(this@All_Products)
//        }
//
//
//        //getting product details from firestore
//        db.collection("seller")
//            .get()
//            .addOnSuccessListener { sellers ->
//                for (i in sellers) {
//                    db.collection("seller")
//                        .document(i.toString())
//                        .collection("products")
//                        .get()
//                        .addOnSuccessListener { products ->
//                            for (j in products) {
//                                data.add(j.toObject(data_all_products::class.java))
//                            }
//                            rv.adapter = Adapter_All_Products(data, this@All_Products)
//                            rv.adapter!!.notifyDataSetChanged()
//                        }
//                        .addOnFailureListener {
//                            Log.d("msg product", "some error retrieving the products")
//                        }
//                }
//            }
//            .addOnFailureListener {
//                Log.d("msg seller", "error retrieving the seller")
//            }
//
    }


}

