package com.example.unicodeinternalhackathon

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SellerProducts : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView : RecyclerView
    val db = Firebase.firestore
    private lateinit var myAdapter: SellerProductsAdapter
    private val productList : ArrayList<data_all_products> = ArrayList()
    private val imageList : ArrayList<Uri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_products)

        recyclerView = findViewById(R.id.rv_seller_products)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

     //   val productNameList = arrayListOf<String>()
        /*val productImageList = arrayListOf<String>()
        val productPriceList = arrayListOf<String?>()*/

        db.collection("seller")
          //  .document(Firebase.auth.currentUser!!.uid)
            .document("33")
            .collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->

               /* querySnapshot.documentChanges.forEach{

                    productList.add(it.document.toObject(data_all_products::class.java))

                    Log.d("msg", productList.toString())



                   *//* productNameList.add(it.document["Name"].toString())
                    productImageList.add(it.document["Image"].toString())
                    productPriceList.add(it.document["DiscountedPrice"].toString())
*//*
                }*/

                querySnapshot.forEach {

                    productList.add(it.toObject(data_all_products::class.java))

                    val filename = it.id
                    val storageRef = FirebaseStorage.getInstance().reference.child("images/$filename")
                    val localFile = File.createTempFile("tempImage","jpg")
                    storageRef.getFile(localFile).addOnSuccessListener {

                        imageList.add(localFile.toUri())

                    }


                }


                myAdapter = SellerProductsAdapter(productList,imageList,this)
                recyclerView.adapter = myAdapter

            }





    }
}

class SellerProductsAdapter(
    private val productList: ArrayList<data_all_products>,
    val  imageList: ArrayList<Uri>,
    val context: Context
) : RecyclerView.Adapter<SellerProductsAdapter.ViewHolder>(){
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val image : ImageView = v.findViewById<ImageView>(R.id.im_all_products_img)
        val name: TextView = v.findViewById<TextView>(R.id.tv_all_products_name)
        val mrp : TextView= v.findViewById<TextView>(R.id.tv_all_products_mrp)
        val discountedPrice : TextView= v.findViewById<TextView>(R.id.tv_all_products_dp)

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

        holder.image.setImageURI(imageList[position])




        holder.itemView.setOnClickListener {


            val activity = it.context as AppCompatActivity
            val intent = Intent(context,ProductDetails::class.java)

            intent.putExtra("product",productList[position])

           activity.startActivity(intent)



        }
    }

    override fun getItemCount(): Int {
     return  productList.size
    }

}