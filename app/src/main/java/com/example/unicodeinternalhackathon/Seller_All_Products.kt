package com.example.unicodeinternalhackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Seller_All_Products : AppCompatActivity() {

    //variables for left nav of seller
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nav: NavigationView

    //rv for recyclerview of all products
    private lateinit var rv: RecyclerView

    //data for adapter of all products
    private lateinit var data: ArrayList<data_all_products>

    //firebase variables
    private val db = Firebase.firestore
    val mAuth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_all_products)

        data = arrayListOf()

        // variable of navigation view,
        // header variable for header of navigation
        //userName variable to access username in header
        nav = findViewById(R.id.seller_nav)
        val header = nav.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.tv_left_nav_name)
        userName.text = mAuth.currentUser!!.displayName

        //assigning toolbar and drawer to work simultaneously
        toolbar = findViewById(R.id.seller_toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.seller_left_nav)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        toggle.isDrawerIndicatorEnabled = true
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //assigning rv and adapter
        rv = findViewById(R.id.rv_seller_products)
        rv.apply {
            layoutManager = LinearLayoutManager(this@Seller_All_Products)
        }

        //getting product details from firestore
        db.collection("seller")
            .get()
            .addOnSuccessListener { sellers ->
                for (i in sellers) {
                    db.collection("seller")
                        .document(i["user_id"].toString())
                        .collection("products")
                        .get()
                        .addOnSuccessListener { products ->
                            for (p in products) {
                                data.add(p.toObject(data_all_products::class.java))
                            }
                            rv.adapter = Adapter_All_Products(data, this)
                            rv.adapter!!.notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            Log.d("msg product", "some error retrieving the products")
                        }
                }
            }
            .addOnFailureListener {
                Log.d("msg seller", "error retrieving the seller")
            }


        nav.setNavigationItemSelectedListener {
            drawer.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.nav_seller_orders -> {
                    val intent = Intent(this, SellerOrders::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_seller_all_products -> {
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.nav_seller_products -> {
                    val intent = Intent(this, SellerAddProduct::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_seller_logout -> {
                    Firebase.auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
//                R.id.nav_seller_min_amount->{
//
//                }
            }
            true
        }
    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu)
//        inflater.inflate(R.menu.menu_seller, menu)
//
//        val search_btn = menu.findItem(R.id.search_buyer)
//        val search = search_btn?.actionView as SearchView
//        search.queryHint = "Search Here"
//
//        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return true
//            }
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != "")
//                {
//                    val new_data = data.filter { data_all_products ->
//                        val s = (data_all_products.Name).lowercase()
//                        newText!!.lowercase().let { s.startsWith(it) }
//                    }
//                    rv.adapter = Adapter_All_Products(new_data as ArrayList<data_all_products>,this@Seller_All_Products)
//                    rv.adapter?.notifyDataSetChanged()
//                }
//                if (newText == "") {
//                    rv.adapter = Adapter_All_Products(data,this@Seller_All_Products)
//                    rv.adapter?.notifyDataSetChanged()
//                }
//                return true
//            }
//
//        })
//    }

}