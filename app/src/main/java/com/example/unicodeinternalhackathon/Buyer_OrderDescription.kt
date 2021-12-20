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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Buyer_OrderDescription : AppCompatActivity() {

    private lateinit var binding: ActivityBuyerOrderDescriptionBinding

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBuyerOrderDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val order = intent.getParcelableExtra<data_orders>("order")

        Glide.with(this)
            .load(order!!.Image)
            .into(binding.ivProductDetailsImage)

        binding.tvProductDetailsName.text = order.Name
        binding.tvProductDetailsDesc.text = order.Description
        binding.tvOrderQuantity.setText(order.Quantity)
        binding.tvTotalAmount.text = order.TotalAmount

        binding.btOrderDetailsEdit.setOnClickListener {

            binding.btOrderDetailsEdit.isVisible = false
            binding.btOrderDetailsUpdate.isVisible = true

            binding.tvOrderQuantity.isEnabled = true

        }

        //changing visibility of edit button as per the status of the order
        db.collection("buyer")
            .document(order.BuyerID)
            .collection("orders")
            .document(order.ProductId)
            .get()
            .addOnSuccessListener {
                if (it["Status"] == "0") {
                    binding.btOrderDetailsEdit.visibility = View.INVISIBLE
                }
                else
                {
                    binding.btOrderDetailsEdit.visibility = View.VISIBLE
                }
            }

        binding.btOrderDetailsUpdate.setOnClickListener {

            val quant = binding.tvOrderQuantity.text.toString()

            val price = order.PICost.toInt()
            val quantity = quant.toInt()
            val amount = price * quantity

            db.collection("buyer")
                .document(Firebase.auth.currentUser!!.uid)
                .collection("orders")
                .document(order.ProductId)
                .update(
                    mapOf(
                        "Quantity" to quant,
                        "TotalAmount" to amount.toString()
                    )
                )
                .addOnSuccessListener {
                    binding.tvTotalAmount.text = amount.toString()
                    Log.d("Order Updated", quant)
                }.addOnFailureListener {
                    Log.d("Update UNSUCCESSFUL", quant)
                }

            binding.btOrderDetailsEdit.isVisible = true
            binding.btOrderDetailsUpdate.isVisible = false

            binding.tvOrderQuantity.isEnabled = false


        }

        binding.delete.setOnClickListener {

            db.collection("buyer")
                .document(Firebase.auth.currentUser!!.uid)
                .collection("orders")
                .document(order.ProductId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Order Removed Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Buyer_orders::class.java))
                }


        }


    }
}