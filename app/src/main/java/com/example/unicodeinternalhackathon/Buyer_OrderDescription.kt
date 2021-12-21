package com.example.unicodeinternalhackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.unicodeinternalhackathon.databinding.ActivityBuyerOrderDescriptionBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Buyer_OrderDescription : AppCompatActivity() {

    private lateinit var binding: ActivityBuyerOrderDescriptionBinding

    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuyerOrderDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val order = intent.getParcelableExtra<data_buyer_orders>("order")
        val mAuth =  Firebase.auth

        Glide.with(this)
            .load(order!!.image)
            .into(binding.ivProductDetailsImage)

        binding.tvProductDetailsName.text = order.name
        binding.tvProductDetailsDesc.text = order.description
        binding.tvOrderQuantity.setText(order.quantity)
        binding.tvTotalAmount.text = order.totalAmount
        binding.tvProductDetailsCompany.text = order.shop_name


        binding.imgBuyerOrderDescBack.setOnClickListener {
            finish()
        }

//        binding.btOrderDetailsEdit.setOnClickListener {
//
//            binding.btOrderDetailsEdit.isVisible = false
//            binding.btOrderDetailsUpdate.isVisible = true
//
//            binding.tvOrderQuantity.isEnabled = true
//
//        }

        //changing visibility of edit button as per the status of the order
//        db.collection("buyer")
//            .document(mAuth.currentUser!!.uid)
//            .collection("orders")
//            .document(order.ProductId)
//            .get()
//            .addOnSuccessListener {
//                if (it["Status"] == "0") {
//                    binding.btOrderDetailsEdit.visibility = View.VISIBLE
//                }
//                else
//                {
//                    binding.btOrderDetailsEdit.visibility = View.INVISIBLE
//                }
//            }
//
//        binding.btOrderDetailsUpdate.setOnClickListener {
//
//            val quant = binding.tvOrderQuantity.text.toString()
//
//            val prevQuantity = order.Quantity.toInt()
//            val price = order.PICost.toInt()
//            val quantity = quant.toInt()
//            val amount = price * quantity
//
//            db.collection("buyer")
//                .document(Firebase.auth.currentUser!!.uid)
//                .collection("orders")
//                .document(order.ProductId)
//                .update(
//                    mapOf(
//                        "Quantity" to quant,
//                        "TotalAmount" to amount.toString()
//                    )
//                )
//                .addOnSuccessListener {
//                    binding.tvTotalAmount.text = amount.toString()
//                    Log.d("Order Updated", quant)
//                        val a  : Long= prevQuantity.toLong()-quantity.toLong()
//                    val b = order.PICost.toLong()*a
//
//                        db.collection("seller")
//                            .document(order.SellerId)
//                            .collection("orders")
//                            .document(order.ProductId)
//                            .update("QuantityFulfilled",FieldValue.increment(-1*a))
//                    db.collection("seller")
//                        .document(order.SellerId)
//                        .collection("orders")
//                        .document(order.ProductId)
//                        .update("TotalAmount",FieldValue.increment(-1*a))
//                        .addOnSuccessListener {
//
//                            db.collection("seller")
//                                .document(order.SellerId)
//                                .collection("orders")
//                                .get()
//                                .addOnSuccessListener {
//                                    // this is like a formula to allot orders are per conditions to seller
//                                    db.collection("seller")
//                                        .document(sId.toString())
//                                        .collection("products")
//                                        .get()
//                                        .addOnSuccessListener { products ->
//                                            tAmount = 0
//                                            for (i in products) {
//                                                //checking if products are above a minimum quantity as told by seller
//                                                if (i["QuantityFulfilled"].toString()
//                                                        .toInt() >= i["MinQuantity"].toString().toInt()
//                                                ) {
//                                                    val sellerOrder = hashMapOf(
//                                                        "Name" to i["Name"],
//                                                        "Quantity" to i["QuantityFulfilled"].toString(),
//                                                        "TotalAmount" to (i["PICost"].toString()
//                                                            .toInt() * i["QuantityFulfilled"].toString()
//                                                            .toInt()).toString(),
//                                                        "Order id" to i["ProductId"].toString()
//                                                    )
//                                                    db.collection("seller")
//                                                        .document(sId.toString())
//                                                        .collection("orders")
//                                                        .document(i["ProductId"].toString())
//                                                        .set(sellerOrder)
//                                                        .addOnSuccessListener {
//                                                            Log.d("order", "order placed for seller")
//                                                        }
//                                                        .addOnFailureListener {
//                                                            Log.d(
//                                                                "order",
//                                                                "order not placed for seller"
//                                                            )
//                                                        }
//
//                                                    db.collection("seller")
//                                                        .document(sId.toString())
//                                                        .collection("products")
//                                                        .document(i["ProductId"].toString())
//                                                        .update("QuantityFulfilled", "0")
//                                                } else {
//                                                    //from here those products will added to seller orders which did not cross
//                                                    //minimum quantity but their sum crosses minimum value
//                                                    tAmount += i["QuantityFulfilled"].toString()
//                                                        .toInt() * i["PICost"].toString().toInt()
//                                                    if (tAmount >= MinAmount) {
//                                                        db.collection("seller")
//                                                            .document(sId.toString())
//                                                            .collection("products")
//                                                            .get()
//                                                            .addOnSuccessListener { products2 ->
//                                                                // iterating and adding only those products which did not cross
//                                                                // minimum quantity
//                                                                for (j in products2) {
//                                                                    if (j["QuantityFulfilled"].toString()
//                                                                            .toInt() < j["MinQuantity"].toString()
//                                                                            .toInt()
//                                                                    ) {
//                                                                        val sellerOrder = hashMapOf(
//                                                                            "Name" to j["Name"],
//                                                                            "Quantity" to j["QuantityFulfilled"].toString(),
//                                                                            "TotalAmount" to (j["PICost"].toString()
//                                                                                .toInt() * j["QuantityFulfilled"].toString()
//                                                                                .toInt()).toString(),
//                                                                            "Order id" to j["ProductId"].toString()
//                                                                        )
//                                                                        db.collection("seller")
//                                                                            .document(sId.toString())
//                                                                            .collection("orders")
//                                                                            .document(j["ProductId"].toString())
//                                                                            .set(sellerOrder)
//                                                                            .addOnSuccessListener {
//                                                                                Log.d(
//                                                                                    "order",
//                                                                                    "order placed for seller"
//                                                                                )
//                                                                            }
//                                                                            .addOnFailureListener {
//                                                                                Log.d(
//                                                                                    "order",
//                                                                                    "order not placed for seller"
//                                                                                )
//                                                                            }
//
//                                                                        db.collection("seller")
//                                                                            .document(sId.toString())
//                                                                            .collection("products")
//                                                                            .document(j["ProductId"].toString())
//                                                                            .update(
//                                                                                "QuantityFulfilled",
//                                                                                "0"
//                                                                            )
//                                                                    }
//                                                                }
//
//                                                            }
//                                                    }
//                                                }
//
//
//
//
//                }.addOnFailureListener {
//                    Log.d("Update UNSUCCESSFUL", quant)
//                }
//
//            binding.btOrderDetailsEdit.isVisible = true
//            binding.btOrderDetailsUpdate.isVisible = false
//
//            binding.tvOrderQuantity.isEnabled = false
//
//
//        }

       /* binding.delete.setOnClickListener {

            db.collection("buyer")
                .document(Firebase.auth.currentUser!!.uid)
                .collection("orders")
                .document(order.ProductId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Order Removed Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Buyer_orders::class.java))
                }


        }*/


    }
}