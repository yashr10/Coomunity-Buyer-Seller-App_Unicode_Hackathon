package com.example.unicodeinternalhackathon

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import android.os.Messenger

import android.content.pm.PackageManager
import java.security.AccessController.getContext


class SellerAddProduct : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    val db = Firebase.firestore
    private lateinit var imageUri: Uri
    private lateinit var productImage: ImageView
    private lateinit var imageText: TextView

    // variables of firebase storage
    private val storage = Firebase.storage
    private val storageRef = storage.reference

    //variable to store imageUrl
    private lateinit var imgUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_add_product)

        val productName: EditText = findViewById(R.id.et_product_name)
        val productDesc: EditText = findViewById(R.id.et_productDesc)
        val productMrp: EditText = findViewById(R.id.et_MRP)
        val productDiscountedPrice: EditText = findViewById(R.id.et_product_discountedPrice)
        val productMinQuantity: EditText = findViewById(R.id.et_minQuantity)
        productImage = findViewById(R.id.iv_productImage)
        imageText = findViewById(R.id.tv_addImageText)
        val addProduct: Button = findViewById(R.id.bt_addProduct)

        if (productName.text.isNullOrBlank() || productDesc.text.isNullOrBlank() || productMrp.text.isNullOrBlank() || productDiscountedPrice.text.isNullOrBlank() || productMinQuantity.text.isNullOrBlank() || imageText.isVisible) {

            Toast.makeText(this, "Please Fill in all details", Toast.LENGTH_SHORT).show()
        }


        imageText.setOnClickListener {

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, 100)
        }

        addProduct.setOnClickListener {
            var productId: String? = null
            db.collection("seller")
                .document(Firebase.auth.currentUser!!.uid)
                .collection("products")
                .document()
                .addSnapshotListener { value, error ->

                    productId = value?.id


                    if (!productId.isNullOrEmpty()) {


                        val filename = productId
                        val a = storageRef.child("image/$filename")
                        storageRef.child("image/$filename").putFile(imageUri)
                            .addOnSuccessListener {

                       //         imgUrl = it.storage.downloadUrl.toString()
                                a.downloadUrl.addOnSuccessListener {

                                    imgUrl = it.toString()
                                    Log.d("image added", imgUrl)
                                }



                                val product = hashMapOf(
                                    "Name" to productName.text.toString(),
                                    "Description" to productDesc.text.toString(),
                                    "MRP" to productMrp.text.toString(),
                                    "DiscountedPrice" to productDiscountedPrice.text.toString(),
                                    "MinQuantity" to productMinQuantity.text.toString(),
                                    "Image" to imgUrl.toString(),
                                    "ProductId" to productId.toString(),
                                    "QuantityFulfilled" to "0",
                                    "SellerId" to mAuth.currentUser!!.uid
                                )

                                db.collection("seller")
                                    .document(Firebase.auth.currentUser!!.uid)
                                    .collection("products")
                                    .document(productId!!)
                                    .set(product)
                                    .addOnSuccessListener {
                                        Log.d("SellerAddProduct", product.toString())

                                        startActivity(Intent(this, SellerProducts::class.java))
                                        finish()

                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            this,
                                            "Failed to add product",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                    } else {
                        Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
                    }


                }


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            imgUrl = imageUri.toString()
            productImage.setImageURI(imageUri)
            Log.d("image", "changed")

            mAuth = FirebaseAuth.getInstance()

            val currentUser = mAuth.currentUser
            imageText.isVisible = false


        } else {
            Log.d("image", "fail")
        }

    }

   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            General.REQUESTPERMISSION -> if (grantResults.length > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                //reload my activity with permission granted or use the features that required the permission
            } else {
               // Messenger.makeToast(getContext(), R.string.noPermissionMarshmallow)
            }
        }
    }*/
}