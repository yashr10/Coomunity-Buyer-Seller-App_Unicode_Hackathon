package com.example.unicodeinternalhackathon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SellerOrders : AppCompatActivity() {

    //variables for left nav of seller
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nav: NavigationView

    private val mAuth = Firebase.auth
    private val db = Firebase.firestore
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: SellerProducts.SellerProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_orders)

        // variable of navigation view,
        // header variable for header of navigation
        //userName variable to access username in header
        nav = findViewById(R.id.seller_orders_nav)
        val header = nav.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.tv_left_nav_name)
        userName.text = mAuth.currentUser!!.displayName

        //assigning toolbar and drawer to work simultaneously
        toolbar = findViewById(R.id.seller_orders_toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.seller_orders_left_nav)
        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        nav.setNavigationItemSelectedListener {
            drawer.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.nav_seller_orders -> {
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.nav_seller_all_products -> {
                    val intent = Intent(this, Seller_All_Products::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_seller_products -> {
                    val intent = Intent(this, SellerProducts::class.java)
                    startActivity(intent)
                    finish()
                }

            }
            true
        }
        recyclerView = findViewById(R.id.rv_seller_orders)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager






    }
}

class SellerOrdersAdapter(
    val context: Context
) : RecyclerView.Adapter<SellerOrdersAdapter.ViewHolder>(){

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val image : ImageView = v.findViewById<ImageView>(R.id.im_all_products_img)
        val name: TextView = v.findViewById<TextView>(R.id.tv_all_products_name)
        val amount: TextView= v.findViewById<TextView>(R.id.tv_all_products_dp)
        val quantity: TextView= v.findViewById<TextView>(R.id.tv_all_products_mrp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val v = inflater.inflate(R.layout.card_activity_seller_order,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val intent = Intent(context,Seller_OrderDescription::class.java)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}