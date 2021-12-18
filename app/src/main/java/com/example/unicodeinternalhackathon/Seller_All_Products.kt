package com.example.unicodeinternalhackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Seller_All_Products : AppCompatActivity() {

    //variables for left nav of seller
    private lateinit var toggle :ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nav:NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_all_products)

        // variable of navigation view,
        // header variable for header of navigation
        //userName variable to access username in header
        nav = findViewById(R.id.seller_nav)
        val header = nav.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.tv_left_nav_name)

        //assigning toolbar and drawer to work simultaneously
        toolbar = findViewById(R.id.seller_toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.seller_left_nav)
        toggle = ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        nav.setNavigationItemSelectedListener {
//            when(it.itemId)
//            {
//                R.id.nav_seller_orders->{
//
//                }
//                R.id.nav_seller_all_products->{
//
//                }
//                R.id.nav_seller_products->{
//
//                }
//                R.id.nav_seller_min_amount->{
//
//                }
//                R.id.nav_seller_profile->{
//
//                }
//            }
//        }
    }

}