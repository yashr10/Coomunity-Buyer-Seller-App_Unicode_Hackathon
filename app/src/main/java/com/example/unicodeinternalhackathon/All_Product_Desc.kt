package com.example.unicodeinternalhackathon

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class All_Product_Desc : AppCompatActivity() {
    //variables of firebase
    private val db = Firebase.firestore
    private val mAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_product_desc)

        //getting data from recyclerview using intent and assigning to the variables
        val name = intent.extras!!.getString("name")
        val img = intent.extras!!.getString("img")
        val desc = intent.extras!!.getString("desc")
        val dp = intent.extras!!.getString("dp")
        val min = intent.extras!!.getString("min")
        val mrp = intent.extras!!.getString("mrp")
        val qf = intent.extras!!.getString("qf")
        val pId = intent.extras!!.getString("pId")
        val sId = intent.extras!!.getString("sId")

        //setting findview by id to put value sin the fields of product details page
        val tvName = findViewById<TextView>(R.id.tv_product_desc_name)
        val imgProd = findViewById<ImageView>(R.id.im_products_desc)
        val tvDesc = findViewById<TextView>(R.id.tv_product_desc_desc)
        val tvDp = findViewById<TextView>(R.id.tv_product_desc_dp)
        val tvMin = findViewById<TextView>(R.id.tv_product_desc_min)
        val tvMrp = findViewById<TextView>(R.id.tv_product_desc_mrp)
        val tvQf = findViewById<TextView>(R.id.tv_product_desc_quantityful)

        //putting data in product details page
        tvName.text = name
        tvDesc.text = desc
        tvDp.text = dp
        tvMin.text = min
        tvMrp.text = mrp
        tvQf.text = qf

        Glide.with(this)
            .load(img.toString())
            .into(imgProd)


        //button for adding requirement
        val req = findViewById<Button>(R.id.bt_prod_desc_req)

        //showing button of adding requirement only to buyer
        db.collection("sellers")
            .whereEqualTo("user_id",mAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                req.visibility =  View.INVISIBLE
            }

        //adding functionality of opening dialog box to button
        req.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.dialog_input, null)
            dialog.setTitle("Add requirement")
            dialog.setPositiveButton("Ok") { _, _ ->
                //adding code to update the quantity of a product when buyer adds his requirement
                val add:String = layout.findViewById<EditText>(R.id.et_dialog_input).toString()
                val total:Int =  add.toInt() + qf!!.toInt()

                tvQf.text = total.toString()

                //storing data to firebase as per the requirement
                db.collection("sellers")
                    .document(sId.toString())
                    .collection("products")
                    .document(pId.toString())
                    .update("QuantityFulfilled",total.toString())
                    .addOnSuccessListener {

                        val tA = tvDp.text.toString().toInt() * add.toInt()

                        val order = hashMapOf(
                            "ProductId" to pId.toString(),
                            "SellerId" to sId.toString(),
                            "BuyerId" to mAuth.currentUser!!.uid,
                            "Quantity" to add,
                            "PICost" to tvDp.text.toString(),
                            "TotalAmount" to tA.toString(),
                            "Image" to img.toString(),
                            "Name" to tvName.toString(),
                            "Description" to desc.toString()
                        )

                        db.collection("buyer")
                            .document(mAuth.currentUser!!.uid)
                            .collection("orders")
                            .document(pId.toString())
                            .set(order)
                            .addOnSuccessListener {
                                Log.d("order msg","data store in buyer order")
                            }
                            .addOnFailureListener {
                                Log.d("order msg","data not stored in buyer order")
                            }
//                        val intent = Intent(this,Buyer_orders::class.java)
//                        intent.putExtra("name",tvName.text.toString())
//                        intent.putExtra("quantity",add)
//                        intent.putExtra("ppItem",tvDp.text.toString())
//                        intent.putExtra("image",img)
                    }
            }
            dialog.setNegativeButton("Cancel") { _, _ ->

            }
            dialog.setView(layout)
            dialog.show()
        }

    }
}