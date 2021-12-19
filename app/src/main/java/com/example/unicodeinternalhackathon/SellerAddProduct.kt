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


class SellerAddProduct : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    val db = Firebase.firestore
    private var imageUri: Uri? = null
    private lateinit var productImage: ImageView
    private lateinit var imageText: TextView

    // variables of firebase storage
    private val storage = Firebase.storage
    private val storageRef = storage.reference

    //variable to store imageUrl
    private var imgUrl: String? = null


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

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            val chooser = Intent.createChooser(intent, "choose")
            intent.type = "image/*"
            startActivityForResult(chooser, 100)
        }

        addProduct.setOnClickListener {
            var productId: String? = null
            db.collection("seller")
//                .document(Firebase.auth.currentUser!!.uid)
                .document("33")
                .collection("products")
                .document()
                .addSnapshotListener { value, error ->

                    productId = value?.id


                    if (!productId.isNullOrEmpty()) {


                        val filename = productId

                        imageUri?.let { it1 -> storageRef.child("image/$filename").putFile(it1) }
                            ?.addOnSuccessListener {
                                storageRef.child("image/$filename").downloadUrl.
                                    addOnSuccessListener {
                                        imgUrl = it.toString()
                                        Log.d("image added", imgUrl.toString())
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
//                                  "SellerId" to mAuth.currentUser!!.uid
                                )

                                db.collection("seller")
                                    .document(Firebase.auth.currentUser!!.uid)
                                    // .document("33")
                                    .collection("products")
                                    .document(productId!!)
                                    .set(product)
                                    .addOnSuccessListener {
                                        Log.d("SellerAddProduct", "Product added")
                                        productName.text = null
                                        productDesc.text = null
                                        productMrp.text = null
                                        productDiscountedPrice.text = null
                                        productMinQuantity.text = null
                                        imageText.isVisible = true
                                        imageUri = null
                                        productImage.setImageURI(imageUri)

                                        startActivity(Intent(this, SellerProducts::class.java))


                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            this,
                                            "Failed to add product",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                            }?.addOnFailureListener {

                                Log.d("iamge exce", it.message.toString())
                            }

                        /* db.collection("seller")
                                .document(Firebase.auth.currentUser!!.uid)
                            // .document("33")
                             .collection("products")
                             .document(productId!!)
                             .set(product)
                             .addOnSuccessListener {
                                 Log.d("SellerAddProduct","Product added")
                                 productName.text = null
                                 productDesc.text = null
                                 productMrp.text = null
                                 productDiscountedPrice.text = null
                                 productMinQuantity.text = null
                                 imageText.isVisible = true
                                 imageUri = null
                                 productImage.setImageURI(imageUri)

                             //    startActivity(Intent(this,SellerProducts::class.java))


                             }.addOnFailureListener {
                                 Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
                             }*/
                    } else {
                        Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
                    }


                }


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            productImage.setImageURI(imageUri)
            Log.d("image", "changed")

            mAuth = FirebaseAuth.getInstance()

            val currentUser = mAuth.currentUser
            imageText.isVisible = false


        } else {
            Log.d("image", "fail")
        }

    }
}