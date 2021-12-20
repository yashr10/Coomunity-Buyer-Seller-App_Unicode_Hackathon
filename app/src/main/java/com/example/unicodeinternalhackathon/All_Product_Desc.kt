package com.example.unicodeinternalhackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_all_product_desc.*

class All_Product_Desc : AppCompatActivity() {
    //variables of firebase
    private val db = Firebase.firestore
    private val mAuth = Firebase.auth
    private var tAmount: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_product_desc)


        img_all_prod_desc_back.setOnClickListener{
            finish()
        }

        //button for adding requirement
        val req = findViewById<Button>(R.id.bt_prod_desc_req)
        req.visibility = View.INVISIBLE


        //showing button of adding requirement only to buyer
        db.collection("buyer")
            .get()
            .addOnSuccessListener { document ->
                for (j in document) {
                    if (j["user_id"] == mAuth.currentUser!!.uid)
                        req.visibility = View.VISIBLE
                }
            }

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

        var MinAmount = 0
        db.collection("seller")
            .document(sId.toString())
            .get()
            .addOnSuccessListener {
                MinAmount = it["MinAmount"].toString().toInt()
                Log.d("min", MinAmount.toString())
            }
        Log.d("min", MinAmount.toString())

        //adding functionality of dialog box for taking req from buyer
        req.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.dialog_input, null)
            dialog.setTitle("Add requirement")
            dialog.setPositiveButton("Add") { _, _ ->
                //adding code to update the quantity of a product when buyer adds his requirement
                val add = layout.findViewById<EditText>(R.id.et_dialog_input)
                val a = add.text.toString().toInt()
                var total: Int = 0
                if (qf.toString().isEmpty()) {
                    total = a
                } else {
                    val b = qf.toString().toInt()
                    total = a + b
                }

                tvQf.text = total.toString()

                //storing data to seller collection in firebase as per the requirement
                db.collection("seller")
                    .document(sId.toString())
                    .collection("products")
                    .document(pId.toString())
                    .update("QuantityFulfilled", total.toString())
                    .addOnSuccessListener {
                        val totalAmount = dp.toString().toInt() * total

                        val order = hashMapOf(
                            "ProductId" to pId.toString(),
                            "SellerId" to sId.toString(),
                            "BuyerId" to mAuth.currentUser!!.uid,
                            "Quantity" to total.toString(),
                            "PICost" to dp.toString(),
                            "TotalAmount" to totalAmount.toString(),
                            "Image" to img.toString(),
                            "Name" to name.toString(),
                            "Description" to desc.toString(),
                            "Status" to "0"
                        )

                        db.collection("buyer")
                            .document(mAuth.currentUser!!.uid)
                            .collection("orders")
                            .document(pId.toString())
                            .set(order)
                            .addOnSuccessListener {

                                // this is like a formula to allot orders are per conditions to seller
                                db.collection("seller")
                                    .document(sId.toString())
                                    .collection("products")
                                    .get()
                                    .addOnSuccessListener { products ->
                                        tAmount = 0
                                        for (i in products) {
                                            //checking if products are above a minimum quantity as told by seller
                                            if (i["QuantityFulfilled"].toString()
                                                    .toInt() >= i["MinQuantity"].toString().toInt()
                                            ) {
                                                val totalAmount2 = i["DiscountedPrice"].toString()
                                                    .toInt() * i["QuantityFulfilled"].toString()
                                                    .toInt()
                                                val sellerOrder = hashMapOf(
                                                    "Name" to i["Name"],
                                                    "Quantity" to i["QuantityFulfilled"].toString(),
                                                    "TotalAmount" to totalAmount2.toString(),
                                                    "OrderId" to i["ProductId"].toString(),
                                                    "Image" to i["Image"].toString(),
                                                    "Description" to i["Description"].toString(),
                                                    "PICost" to i["DiscountedPrice"].toString(),
                                                    "Status" to "0",
                                                )

                                                db.collection("seller")
                                                    .document(sId.toString())
                                                    .collection("orders")
                                                    .document(i["ProductId"].toString())
                                                    .set(sellerOrder)
                                                    .addOnSuccessListener {
                                                        Log.d("order", "order placed for seller")
                                                    }
                                                    .addOnFailureListener {
                                                        Log.d(
                                                            "order",
                                                            "order not placed for seller"
                                                        )
                                                    }

                                                db.collection("seller")
                                                    .document(sId.toString())
                                                    .collection("products")
                                                    .document(i["ProductId"].toString())
                                                    .update("QuantityFulfilled", "0")

                                            } else {
                                                //from here those products will added to seller orders which did not cross
                                                //minimum quantity but their sum crosses minimum value
                                                tAmount += (i["QuantityFulfilled"].toString().toInt() * i["DiscountedPrice"].toString().toInt())

                                                if (tAmount >= MinAmount) {
                                                    db.collection("seller")
                                                        .document(sId.toString())
                                                        .collection("products")
                                                        .get()
                                                        .addOnSuccessListener { products2 ->
                                                            // iterating and adding only those products which did not cross
                                                            // minimum quantity
                                                            for (j in products2) {
                                                                if (j["QuantityFulfilled"].toString().toInt() < j["MinQuantity"].toString().toInt()){
                                                                    db.collection("seller")
                                                                        .document(sId.toString())
                                                                        .collection("orders")
                                                                        .get()
                                                                        .addOnSuccessListener { orders ->
                                                                            Log.d("ms1", "Hello")
                                                                            Log.d(
                                                                                "size",
                                                                                orders.size()
                                                                                    .toString()
                                                                            )
                                                                            val x = orders.size()
                                                                            if (x == 0 && j["QuantityFulfilled"].toString().toInt()!=0) {
                                                                                val sellerOrder =
                                                                                    hashMapOf(
                                                                                        "Name" to j["Name"],
                                                                                        "Quantity" to j["QuantityFulfilled"].toString(),
                                                                                        "TotalAmount" to (j["DiscountedPrice"].toString()
                                                                                            .toInt() * j["QuantityFulfilled"].toString()
                                                                                            .toInt()).toString(),
                                                                                        "Order id" to j["ProductId"].toString(),
                                                                                        "Image" to j["Image"].toString(),
                                                                                        "Description" to j["Description"].toString(),
                                                                                        "PICost" to j["DiscountedPrice"].toString(),
                                                                                        "Status" to "0"
                                                                                    )
                                                                                db.collection("seller")
                                                                                    .document(sId.toString())
                                                                                    .collection("orders")
                                                                                    .document(j["ProductId"].toString())
                                                                                    .set(sellerOrder)
                                                                                    .addOnSuccessListener {
                                                                                        Log.d(
                                                                                            "order",
                                                                                            "order placed for seller"
                                                                                        )
                                                                                    }
                                                                                    .addOnFailureListener {
                                                                                        Log.d(
                                                                                            "order",
                                                                                            "order not placed for seller"
                                                                                        )
                                                                                    }

                                                                                db.collection("seller")
                                                                                    .document(sId.toString())
                                                                                    .collection("products")
                                                                                    .document(j["ProductId"].toString())
                                                                                    .update(
                                                                                        "QuantityFulfilled",
                                                                                        "0"
                                                                                    )
                                                                            } else {
                                                                                var count = 0;
                                                                                for(k in orders)
                                                                                {
                                                                                    if(j["ProductId"].toString() != k["Order id"].toString())
                                                                                    {
                                                                                        count+=1
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        break
                                                                                    }
                                                                                }

                                                                                for (k in orders) {
                                                                                    if (count!=orders.size()) {
                                                                                        Log.d("id", j["ProductId"].toString())
                                                                                        Log.d(
                                                                                            "id",
                                                                                            k["Order id"].toString()
                                                                                        )
                                                                                        val sellerOrder =
                                                                                            hashMapOf(
                                                                                                "Name" to j["Name"],
                                                                                                "Quantity" to j["QuantityFulfilled"].toString(),
                                                                                                "TotalAmount" to (j["DiscountedPrice"].toString()
                                                                                                    .toInt() * j["QuantityFulfilled"].toString()
                                                                                                    .toInt()).toString(),
                                                                                                "Order id" to j["ProductId"].toString(),
                                                                                                "Image" to j["Image"].toString(),
                                                                                                "Description" to j["Description"].toString(),
                                                                                                "PICost" to j["DiscountedPrice"].toString(),
                                                                                                "Status" to "0"
                                                                                            )
                                                                                        db.collection(
                                                                                            "seller"
                                                                                        )
                                                                                            .document(sId.toString())
                                                                                            .collection("orders")
                                                                                            .document(j["ProductId"].toString())
                                                                                            .set(sellerOrder)
                                                                                            .addOnSuccessListener {
                                                                                                Log.d("order", "order placed for seller")
                                                                                            }
                                                                                            .addOnFailureListener {
                                                                                                Log.d("order", "order not placed for seller"
                                                                                                )
                                                                                            }

                                                                                        db.collection("seller")
                                                                                            .document(
                                                                                                sId.toString()
                                                                                            )
                                                                                            .collection("products")
                                                                                            .document(j["ProductId"].toString())
                                                                                            .update("QuantityFulfilled", "0")
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        .addOnFailureListener {
                                                                            Log.d("ms","Hello")
                                                                        }
                                                                }
                                                            }

                                                        }
                                                }
                                            }
                                        }
                                    }
                                val intent = Intent(this, Buyer_orders::class.java)
                                startActivity(intent)
                                finish()
                                Log.d("order msg", "data store in buyer order")
                            }
                            .addOnFailureListener {
                                Log.d("order msg", "data not stored in buyer order")
                            }
                    }
                    .addOnFailureListener {
                        Log.d("order msg", "somme error")
                    }
            }
            dialog.setNegativeButton("Cancel") { _, _ ->

            }
            dialog.setView(layout)
            dialog.show()
        }

    }

}