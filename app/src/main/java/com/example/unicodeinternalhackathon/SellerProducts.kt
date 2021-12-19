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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SellerProducts : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView : RecyclerView
    val db = Firebase.firestore
    private lateinit var myAdapter: SellerProductsAdapter

    private val imageList : ArrayList<Uri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_products)
        Log.d("OnCreate","reached")

        recyclerView = findViewById(R.id.rv_seller_products)
         val productList : ArrayList<data_all_products> = ArrayList()
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager


        db.collection("seller")
           .document(Firebase.auth.currentUser!!.uid)
//            .document("33")
            .collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->

                querySnapshot.documentChanges.forEach{

                    productList.add(it.document.toObject(data_all_products::class.java))

                    Log.d("msg", productList.toString())

                }

                myAdapter = SellerProductsAdapter(productList,this)
                recyclerView.adapter = myAdapter

            }

        val floatingActionButton : FloatingActionButton = findViewById(R.id.floatingButton)

        floatingActionButton.setOnClickListener {

            startActivity(Intent(this,SellerAddProduct::class.java))

        }


    }

    override fun onPause() {
        super.onPause()
        Log.d("ACtivity","onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ACtivity","onResume")
        val productList : ArrayList<data_all_products> = ArrayList()

        db.collection("seller")
            .document(Firebase.auth.currentUser!!.uid)
//            .document("33")
            .collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->

                querySnapshot.documentChanges.forEach{

                    productList.add(it.document.toObject(data_all_products::class.java))

                    Log.d("msg", productList.toString())

                }

                myAdapter = SellerProductsAdapter(productList,this)
                recyclerView.adapter = myAdapter
                recyclerView.adapter!!.notifyDataSetChanged()

            }
    }
}

class SellerProductsAdapter(
    private val productList: ArrayList<data_all_products>,
    val context: Context
) : RecyclerView.Adapter<SellerProductsAdapter.ViewHolder>(){
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val image : ImageView = v.findViewById(R.id.im_all_products_img)
        val name: TextView = v.findViewById(R.id.tv_all_products_name)
        val mrp : TextView= v.findViewById(R.id.tv_all_products_mrp)
        val discountedPrice : TextView= v.findViewById(R.id.tv_all_products_dp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val v = inflater.inflate(R.layout.card_view_all_products,parent,false)

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

            val activity = it.context as AppCompatActivity
            val intent = Intent(context, ProductDetails::class.java)

            intent.putExtra("product",productList[position])

           activity.startActivity(intent)



        }
    }

    override fun getItemCount(): Int {
     return  productList.size
    }

}