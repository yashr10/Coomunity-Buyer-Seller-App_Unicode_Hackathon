package com.example.unicodeinternalhackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.unicodeinternalhackathon.databinding.ActivitySellerOrderDescriptionBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Seller_OrderDescription : AppCompatActivity() {

    private lateinit var binding : ActivitySellerOrderDescriptionBinding
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerOrderDescriptionBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val order = intent.getParcelableExtra<data_seller_order>("Seller order")

        if (order?.Status != "0"){

            binding.btAccept.isVisible  =false
            binding.btReject.isVisible = false
        }

        val name = binding.tvProductDetailsName
        val description = binding.tvProductDetailsDesc
        val quantity  =binding.tvProductDetailsMrp
        val amount = binding.tvProductDetailsDp
        val image = binding.ivProductDetailsImage

        name.text = order?.name
        description.text = order?.description
        quantity.text  =order?.quantity
        amount.text = order?.totalAmount

        Glide.with(this)
            .load(order?.image)
            .into(image)

        binding.btAccept.setOnClickListener {


            db.collection("seller")
                .document(Firebase.auth.currentUser!!.uid)
                .collection("sOrder")
                .document(order?.orderId.toString())
                .update("Status","1")
                .addOnSuccessListener {
                    startActivity(Intent(this,SellerOrders::class.java))
                }



        }

        binding.btReject.setOnClickListener {


            db.collection("seller")
                .document(Firebase.auth.currentUser!!.uid)
                .collection("sOrder")
                .document(order?.orderId.toString())
                .update("Status","2")
                .addOnSuccessListener {
                    startActivity(Intent(this,SellerOrders::class.java))
                }
        }
        binding.back.setOnClickListener {

            startActivity(Intent(this,SellerOrders::class.java))
        }


    }
}