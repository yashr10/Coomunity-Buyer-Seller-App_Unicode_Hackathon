package com.example.unicodeinternalhackathon

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class SellerProducts : AppCompatActivity() {

    //variables for left nav of seller
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nav: NavigationView

    private val mAuth = Firebase.auth

    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView
    val db = Firebase.firestore
    private lateinit var myAdapter: SellerProductsAdapter
    private lateinit var productList: ArrayList<data_all_products>

    var MinAmount:String = ""
    var origin:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_products)


        productList = arrayListOf()


        Log.d("OnCreate", "reached")


        try {
            origin = intent.extras!!.getString("origin").toString()

            if(origin == "Register")
            {
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
                dialog.setCancelable(false)
                dialog.setView(view)
                dialog.show()
            }

        }
        catch(e:Exception){
            Log.d("msg",e.message.toString())
        }


        val text : TextView = findViewById(R.id.tv)

        db.collection("seller")
            .document(Firebase.auth.currentUser!!.uid)
            .collection("products")
            .get()
            .addOnSuccessListener { documents ->
                for(i in documents)
                {

                    productList.add(i.toObject(data_all_products::class.java))
                }

                if (productList.isEmpty()){

                    recyclerView.isVisible = false
                }else{

                    recyclerView = findViewById(R.id.rv_seller_products)
                    linearLayoutManager = LinearLayoutManager(this)
                    recyclerView.layoutManager = linearLayoutManager
                    myAdapter = SellerProductsAdapter(productList, this)
                    recyclerView.adapter = myAdapter
                }



            }

        val floatingActionButton: FloatingActionButton = findViewById(R.id.floatingButton)



        floatingActionButton.setOnClickListener {

            startActivity(Intent(this, SellerAddProduct::class.java))

        }

//         variable of navigation view,
//         header variable for header of navigation
//        userName variable to access username in header
        nav = findViewById(R.id.seller_products_nav1)
        val header = nav.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.tv_left_nav_name)
        userName.text = mAuth.currentUser!!.displayName

//        assigning toolbar and drawer to work simultaneously
        toolbar = findViewById(R.id.seller_products_toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.seller_products_left_nav)
        toggle = ActionBarDrawerToggle(this, drawer,toolbar, R.string.open, R.string.close)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        toggle.isDrawerIndicatorEnabled = true
        drawer.addDrawerListener(toggle)
        toggle.syncState()

//        setting nav for accessing activities through left nav
        nav.setNavigationItemSelectedListener {
            drawer.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.nav_seller_orders -> {
                    val intent = Intent(this,SellerProducts::class.java)
                    intent.putExtra("origin","Seller Products")
                    startActivity(intent)
                    finish()

                }
                R.id.nav_seller_all_products -> {
                    val intent = Intent(this, Seller_All_Products::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_seller_products -> {
                    drawer.closeDrawer(GravityCompat.START)
                }
                R.id.nav_seller_logout -> {
                    Firebase.auth.signOut()
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
            }
            true
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.d("ACtivity", "onRestart")
//        val productList: ArrayList<data_all_products> = ArrayList()
//

        productList = arrayListOf()
        db.collection("seller")
            .document(Firebase.auth.currentUser!!.uid)
            .collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->

                querySnapshot.forEach {

                    productList.add(it.toObject(data_all_products::class.java))

                }


                myAdapter = SellerProductsAdapter(productList, this)
                recyclerView.adapter = myAdapter

                recyclerView.adapter!!.notifyDataSetChanged()

            }
    }


    class SellerProductsAdapter(
        private val productList: ArrayList<data_all_products>,
        val context: Context
    ) : RecyclerView.Adapter<SellerProductsAdapter.ViewHolder>() {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            val image: ImageView = v.findViewById(R.id.im_all_products_img)
            val name: TextView = v.findViewById(R.id.tv_all_products_name)
            val mrp: TextView = v.findViewById(R.id.tv_all_products_mrp)
            val discountedPrice: TextView = v.findViewById(R.id.tv_all_products_dp)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            val v = inflater.inflate(R.layout.card_view_all_products, parent, false)

            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.name.text = productList[position].Name
            holder.discountedPrice.text = productList[position].DiscountedPrice
            holder.mrp.text = productList[position].MRP

            Glide.with(context)
                .load(productList[position].Image)
                .into(holder.image)
//        holder.image.setImageURI(productList[position].Image.toUri())
            //   holder.image.setImageURI(imageList[position])


            holder.itemView.setOnClickListener {

                val intent = Intent(context, ProductDetails::class.java)

                intent.putExtra("product", productList[position])

                context.startActivity(intent)


            }
        }

        override fun getItemCount(): Int {
            return productList.size
        }

    }
}