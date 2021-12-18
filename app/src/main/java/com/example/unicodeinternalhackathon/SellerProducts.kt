package com.example.unicodeinternalhackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SellerProducts : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView : RecyclerView
    val db = Firebase.firestore
    private lateinit var myAdapter: SellerProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_products)

        recyclerView = findViewById(R.id.rv_seller_products)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        val productNameList = arrayListOf<String>()
        val productImageList = arrayListOf<String>()
        val productPriceList = arrayListOf<String?>()

        db.collection("seller")
          //  .document(Firebase.auth.currentUser!!.uid)
            .document()
            .collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->

                querySnapshot.documentChanges.forEach{

                    productNameList.add(it.document["Name"].toString())
                    productImageList.add(it.document["Image"].toString())
                    productPriceList.add(it.document["DiscountedPrice"].toString())

                }

                myAdapter = SellerProductsAdapter(productNameList,productPriceList,productImageList)
                recyclerView.adapter = myAdapter

            }





    }
}

class SellerProductsAdapter(
    val productNameList: ArrayList<String>,
   val  productPriceList: ArrayList<String?>,
   val  productImageList: ArrayList<String>
) : RecyclerView.Adapter<SellerProductsAdapter.ViewHolder>(){
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val image : ImageView = v.findViewById<ImageView>(R.id.iv_product_image)
        val name: TextView = v.findViewById<TextView>(R.id.tv_product_name)
        val price : TextView= v.findViewById<TextView>(R.id.tv_product_price)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val v = inflater.inflate(R.layout.card_seller_products,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = productNameList[position]
        holder.price.text = productPriceList[position]
        holder.image.setImageURI(productImageList[position].toUri())

        holder.itemView.setOnClickListener {


        }
    }

    override fun getItemCount(): Int {
     return  productNameList.size
    }

}