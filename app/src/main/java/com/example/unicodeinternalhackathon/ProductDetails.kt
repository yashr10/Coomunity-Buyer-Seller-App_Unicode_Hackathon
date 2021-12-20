package com.example.unicodeinternalhackathon

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.unicodeinternalhackathon.databinding.ActivityProductDetailsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductDetails : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private var imageUri : String = ""
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getParcelableExtra<data_all_products>("product")!!

        binding.tvProductDetailsName.setText(product.Name)
        binding.tvProductDetailsDesc.setText(product.Description)
        binding.tvProductDetailsMrp.setText(product.MRP)
        binding.tvProductDetailsDp.setText(product.DiscountedPrice)
        binding.tvProductDetailsMinQuantity.setText(product.MinQuantity)

        Glide.with(this)
            .load(product.Image)
            .into(binding.ivProductDetailsImage)

        imageUri = product.Image


        binding.imgProdDetailsBack.setOnClickListener {
            finish()
        }

        binding.delete.setOnClickListener{

            db.collection("seller")
                   .document(Firebase.auth.currentUser!!.uid)
             //   .document("33")
                .collection("products")
                .document(product.ProductId)
                .delete()
                .addOnSuccessListener {

                    Toast.makeText(this, "Product Removed Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,SellerProducts::class.java))

                }


        }

        binding.btProductDetailsEdit.setOnClickListener {


            binding.tvProductDetailsName.isEnabled = true
            binding.tvProductDetailsDesc.isEnabled = true
            binding.tvProductDetailsMrp.isEnabled = true
            binding.tvProductDetailsDp.isEnabled = true
            binding.tvProductDetailsMinQuantity.isEnabled = true
            binding.ivProductDetailsImage.isClickable = true

            binding.btProductDetailsUpdate.isVisible = true
            binding.btProductDetailsEdit.isVisible = false

        }

        binding.btProductDetailsUpdate.setOnClickListener {


            binding.tvProductDetailsName.isEnabled = false
            binding.tvProductDetailsDesc.isEnabled = false
            binding.tvProductDetailsMrp.isEnabled = false
            binding.tvProductDetailsDp.isEnabled = false
            binding.tvProductDetailsMinQuantity.isEnabled = false
            binding.ivProductDetailsImage.isEnabled = false

            binding.btProductDetailsUpdate.isVisible = false
            binding.btProductDetailsEdit.isVisible = true



            val input = hashMapOf(

                "Name" to binding.tvProductDetailsName.text.toString(),
                "Description" to binding.tvProductDetailsDesc.text.toString(),
                "MRP" to  binding.tvProductDetailsMrp.text.toString(),
                "DiscountedPrice"  to  binding.tvProductDetailsDp.text.toString(),
                "MinQuantity" to  binding.tvProductDetailsMinQuantity.text.toString(),
                "Image" to imageUri,
                "ProductId" to product.ProductId,
                "SellerId" to Firebase.auth.currentUser!!.uid

            )

            db.collection("seller")
                   .document(Firebase.auth.currentUser!!.uid)
      //          .document("33")
                .collection("products")
                .document(product.ProductId)
                .set(input)
                .addOnSuccessListener {

                    Log.d("Product","updated")

                }




        }





    }
}