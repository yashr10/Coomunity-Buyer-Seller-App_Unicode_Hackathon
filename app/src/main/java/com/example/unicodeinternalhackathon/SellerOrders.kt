package com.example.unicodeinternalhackathon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
    private lateinit var myAdapter: SellerOrdersAdapter
    val orderList : ArrayList<data_seller_order> = ArrayList()
//    private lateinit var data:ArrayList<data_seller_order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_orders)

//        data = arrayListOf()

        // variable of navigation view,
        // header variable for header of navigation
        //userName variable to access username in header
        nav = findViewById(R.id.seller_orders_nav)
        val header = nav.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.tv_left_nav_name)
        userName.text = mAuth.currentUser!!.displayName
        var MinAmount: String? = ""

        //assigning toolbar and drawer to work simultaneously
        toolbar = findViewById(R.id.seller_orders_toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.seller_orders_left_nav)
        toggle = ActionBarDrawerToggle(this, drawer,toolbar, R.string.open, R.string.close)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        toggle.isDrawerIndicatorEnabled = true
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //assigning header username
        db.collection("seller").document(mAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                userName.text = it["shop_name"].toString()
            }

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
                R.id.nav_seller_logout -> {
                Firebase.auth.signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
                }
                R.id.nav_seller_min_amount->{
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Minimum Order Amount")
                    dialog.setMessage("Enter Minimum Order Amount")
                    val inflater = layoutInflater
                    val view = inflater.inflate(R.layout.dialog_input,null)
                    dialog.setPositiveButton("Save"){_,_ ->
                        val input = view.findViewById<EditText>(R.id.et_dialog_input)
                        MinAmount = input.text.toString()
                        db.collection("seller")
                            .document(mAuth.currentUser!!.uid)
                            .update("MinAmount",MinAmount)
                    }
                    dialog.setNegativeButton("Cancel"){_,_ ->

                    }
                    dialog.setView(view)
                    dialog.show()
                }

            }
            true
        }
        recyclerView = findViewById(R.id.rv_seller_orders)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

     /*   val sellerOrder = hashMapOf(
            "Name" to i["Name"],
            "Quantity" to i["QuantityFulfilled"].toString(),
            "TotalAmount" to (i["DiscountedPrice" +
                    "" +
                    ""].toString()
                .toInt() * i["QuantityFulfilled"].toString()
                .toInt()).toString(),
            "OrderId" to i["ProductId"].toString(),
            "Image" to i["Image"].toString(),
            "Description" to i["Description"].toString()

        )

        db.collection("seller")
            .document(sId.toString())
            .collection("orders")
            .document(i["ProductId"].toString())
            .set(sellerOrder)*/



        db.collection("seller")
            .document(Firebase.auth.currentUser!!.uid)
            .collection("sOrder")
            .get()
            .addOnSuccessListener {

                it.forEach { queryDocumentSnapshot ->

                    orderList.add(queryDocumentSnapshot.toObject(data_seller_order::class.java))

                }

                if (orderList.isEmpty()){

                    recyclerView.isVisible =false
                }else{

                    myAdapter = SellerOrdersAdapter(orderList,this)
                    recyclerView.adapter = myAdapter
                    Log.d("Seller Order",orderList.toString())
                    findViewById<TextView>(R.id.tv_text).isVisible = false
                }

            }




    }

    override fun onRestart() {
        super.onRestart()
        val orderList : ArrayList<data_seller_order> = ArrayList()

        db.collection("seller")
            .document(Firebase.auth.currentUser!!.uid)
            .collection("sOrder")
            .get()
            .addOnSuccessListener {

                it.forEach { queryDocumentSnapshot ->

                    orderList.add(queryDocumentSnapshot.toObject(data_seller_order::class.java))

                }
                myAdapter = SellerOrdersAdapter(orderList,this)
                recyclerView.adapter = myAdapter
            }
    }

    //search bar code
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val search_btn = menu.findItem(R.id.search)
        val search = search_btn?.actionView as SearchView
        search.queryHint = "Search Here"


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "")
                {
                    val new_data = orderList.filter { data_seller_order ->
                        val s = (data_seller_order.name).lowercase()
                        newText!!.lowercase().let { s.startsWith(it) }
                    }
                    recyclerView.adapter = SellerOrdersAdapter(new_data as ArrayList<data_seller_order>, context = this@SellerOrders)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                if (newText == "") {
                    recyclerView.adapter = SellerOrdersAdapter(orderList, context = this@SellerOrders)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                return true
            }

        })
        return true
    }
}

class SellerOrdersAdapter(
    val orderList: ArrayList<data_seller_order>,
    val context: Context
) : RecyclerView.Adapter<SellerOrdersAdapter.ViewHolder>(){

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val image : ImageView = v.findViewById<ImageView>(R.id.im_all_products_img)
        val name: TextView = v.findViewById<TextView>(R.id.tv_all_products_name)
        val amount: TextView= v.findViewById<TextView>(R.id.tv_all_products_dp)
        val quantity: TextView= v.findViewById<TextView>(R.id.tv_all_products_mrp)
        val status: TextView= v.findViewById<TextView>(R.id.tv_status)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val v = inflater.inflate(R.layout.card_activity_seller_order,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = orderList[position].name
        holder.amount.text = orderList[position].totalAmount
        holder.quantity.text = orderList[position].quantity

        Glide.with(context)
            .load(orderList[position].image)
            .into(holder.image)

        when(orderList[position].Status){

            "0" -> holder.status.isVisible = false
            "1"-> holder.status.isVisible = true
            "2"-> {
                holder.status.isVisible = true
                holder.status.text = "Rejected"
                holder.status.setTextColor( ContextCompat.getColor(context, R.color.design_default_color_error))

            }
        }



        holder.itemView.setOnClickListener {
            val intent = Intent(context,Seller_OrderDescription::class.java)
            intent.putExtra("Seller order",orderList[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
      return orderList.size
    }


}