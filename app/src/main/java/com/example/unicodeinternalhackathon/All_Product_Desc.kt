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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_all_product_desc.*
import java.util.*

class All_Product_Desc : AppCompatActivity() {
    //variables of firebase
    private val db = Firebase.firestore
    private val mAuth = Firebase.auth
    private var tAmount: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_product_desc)


        img_all_prod_desc_back.setOnClickListener {
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

        val pId = intent.extras!!.getString("pId")
        val sId = intent.extras!!.getString("sId")
        val shop = intent.extras!!.getString("shop_name")

        //setting findview by id to put value sin the fields of product details page
        val tvName = findViewById<TextView>(R.id.tv_product_desc_name)
        val imgProd = findViewById<ImageView>(R.id.im_products_desc)
        val tvDesc = findViewById<TextView>(R.id.tv_product_desc_desc)
        val tvDp = findViewById<TextView>(R.id.tv_product_desc_dp)
        val tvMin = findViewById<TextView>(R.id.tv_product_desc_min)
        val tvMrp = findViewById<TextView>(R.id.tv_product_desc_mrp)
        val company = findViewById<TextView>(R.id.company)

        //putting data in product details page
        tvName.text = name
        tvDesc.text = desc
        tvDp.text = dp
        tvMin.text = min
        tvMrp.text = mrp
        company.append(shop)


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
                val quant = add.text.toString().toInt()


                val total = quant *dp.toString().toInt()


                val orderId = UUID.randomUUID()
                val userOrderId = UUID.randomUUID()

                db.collection("orders")
                    .document(sId.toString())
                    .collection("mOrders")
                    .get()
                    .addOnSuccessListener { mainDoc -> //SL1

                        Log.d("listner", "success")
                        var totalAmount1 = 0
                        var isSame = false
                        var minQuant = false
                        var sameId = ""

                        mainDoc.forEach { document ->


                            if (document["usable"].toString() == "1") {
                                totalAmount1 += document["totalAmount"].toString().toInt()
                            }


                            if (document["productId"].toString() == pId.toString() && document["usable"].toString() == "1") {

                                isSame = true
                                sameId = document["orderId"].toString()
                                val totalAmount = document["totalAmount"].toString().toInt() + total // total amount of the product order you are placing
                                val quantity = document["quantity"].toString().toInt() + quant

                                Log.d("for each if", totalAmount.toString())
                                Log.d("for each if", quantity.toString())

                                db.collection("orders")
                                    .document(sId.toString())
                                    .collection("mOrders")
                                    .document(document["orderId"].toString())
                                    .update(
                                        mapOf(
                                            "totalAmount" to totalAmount.toString(),
                                            "quantity" to quantity.toString(),
                                            "buyerAl" to FieldValue.arrayUnion(mAuth.currentUser!!.uid),
                                            "buyerOrderIdAl" to FieldValue.arrayUnion(userOrderId.toString())
                                        )
                                    )

                                if (quantity >= min.toString().toInt()) {
                                    Log.d("for each if inside", "quantity >")
                                    minQuant = true
                                    val sOrder = hashMapOf(
                                        "quantity" to quantity.toString(),
                                        "totalAmount" to totalAmount.toString(),
                                        "productId" to document["productId"].toString(),
                                        "name" to document["name"].toString(),
                                        "image" to document["image"].toString(),
                                        "description" to document["description"].toString(),
                                        "Status" to "0",
                                        "orderId" to document["orderId"].toString(),
                                        "buyerOrderIdAl" to document["buyerOrderIdAl"]


                                        )
                                    db.collection("seller")
                                        .document(sId.toString())
                                        .collection("sOrder")
                                        .document(document["orderId"].toString())
                                        .set(sOrder)

                                    //place buyer order
                                    val bOrder = hashMapOf(
                                        "quantity" to quant.toString(),
                                        "totalAmount" to total.toString(),
                                        "sellerId" to sId.toString(),
                                        "productId" to pId.toString(),
                                        "name" to name.toString(),
                                        "image" to img.toString(),
                                        "description" to desc.toString(),
                                        "Status" to "3",
                                        "userOrderId" to userOrderId.toString(),
                                        "orderId" to document["orderId"].toString(),
                                        "discountedPrice" to dp.toString(),
                                        "mrp" to mrp.toString(),
                                        "minAmount" to MinAmount.toString(),
                                        "shop_name" to shop.toString()

                                    )


                                    db.collection("buyer")
                                        .document(mAuth.currentUser!!.uid)
                                        .collection("bOrders")
                                        .document(userOrderId.toString())
                                        .set(bOrder)
                                        .addOnSuccessListener {

                                        }
                                    db.collection("orders")
                                        .document(sId.toString())
                                        .collection("mOrders")
                                        .document(document["orderId"].toString())
                                        .update("usable", "0")


                                } //quantity >= min.toString().toInt()


                            }  // if khatam


                        }
                        if (isSame && !minQuant) {

                            val t = total + totalAmount1
                            Log.d("same total equla", t.toString())

                            //place buyer order
                            val bOrder = hashMapOf(
                                "quantity" to quant.toString(),
                                "totalAmount" to total.toString(),
                                "sellerId" to sId.toString(),
                                "productId" to pId.toString(),
                                "name" to name.toString(),
                                "image" to img.toString(),
                                "description" to desc.toString(),
                                "Status" to "3",
                                "userOrderId" to userOrderId.toString(),
                                "orderId" to sameId,
                                "discountedPrice" to dp.toString(),
                                "mrp" to mrp.toString(),
                                "minAmount" to MinAmount.toString(),
                                "shop_name" to shop.toString()

                            )

                            db.collection("buyer")
                                .document(mAuth.currentUser!!.uid)
                                .collection("bOrders")
                                .document(userOrderId.toString())
                                .set(bOrder)
                                .addOnSuccessListener {

                                }
                            if (t > MinAmount) {

                                Log.d("t>minamo", t.toString())

                                //place seller order
                                db.collection("orders")
                                    .document(sId.toString())
                                    .collection("mOrders")
                                    .get()
                                    .addOnSuccessListener { mainDo ->

                                        mainDo.forEach { document ->


                                            val sOrder = hashMapOf(
                                                "quantity" to document["quantity"].toString(),
                                                "totalAmount" to document["totalAmount"].toString(),
                                                "productId" to document["productId"].toString(),
                                                "name" to document["name"].toString(),
                                                "image" to document["image"].toString(),
                                                "description" to document["description"].toString(),
                                                "Status" to "0",
                                                "orderId" to document["orderId"].toString(),
                                                "buyerOrderIdAl" to document["buyerOrderIdAl"]

                                            )
                                            db.collection("seller")
                                                .document(sId.toString())
                                                .collection("sOrder")
                                                .document(document["orderId"].toString())
                                                .set(sOrder)
                                                .addOnSuccessListener {
                                                    Log.d("sOrder", "placed")
                                                }.addOnFailureListener {
                                                    Log.d("sOrder", it.message.toString())
                                                }

                                            db.collection("orders")
                                                .document(sId.toString())
                                                .collection("mOrders")
                                                .document(document["orderId"].toString())
                                                .update("usable", "0")
                                        }

                                    }


                            }


                        }
                        if (!isSame) {

                            //place buyer order

                            Log.d("reached", "'here")
                            val bOrder = hashMapOf(
                                "quantity" to quant.toString(),
                                "totalAmount" to total.toString(),
                                "sellerId" to sId.toString(),
                                "productId" to pId.toString(),
                                "name" to name.toString(),
                                "image" to img.toString(),
                                "description" to desc.toString(),
                                "Status" to "3",
                                "userOrderId" to userOrderId.toString(),
                                "orderId" to orderId.toString(),
                                "discountedPrice" to dp.toString(),
                                "mrp" to mrp.toString(),
                                "minAmount" to MinAmount.toString(),
                                "shop_name" to shop.toString()

                            )

                            db.collection("buyer")
                                .document(mAuth.currentUser!!.uid)
                                .collection("bOrders")
                                .document(userOrderId.toString())
                                .set(bOrder)
                                .addOnSuccessListener {

                                }


                            val buyerAl = arrayListOf<String>()
                            buyerAl.add(mAuth.currentUser!!.uid)
                            val buyerOrderIdAl = arrayListOf<String>()
                            buyerOrderIdAl.add(userOrderId.toString())


                            when {

                                quant>=min.toString().toInt() -> {

                                    Log.d("quant",quant.toString())


                                    Log.d("quant", quant.toString())


                                    val order = hashMapOf(
                                        "totalAmount" to total.toString(),
                                        "quantity" to quant.toString(),
                                        "buyerAl" to buyerAl,
                                        "buyerOrderIdAl" to buyerOrderIdAl,
                                        "usable"   to "0",
                                        "productId" to pId.toString(),
                                        "orderId" to orderId.toString(),
                                        "name" to name.toString(),
                                        "image" to img.toString(),
                                        "description" to desc.toString()
                                    )

                                    db.collection("orders")
                                        .document(sId.toString())
                                        .collection("mOrders")
                                        .document(orderId.toString())
                                        .set(order)
                                        .addOnSuccessListener {

                                            Log.d("ORDER","received 1")

                                        }
                                    val sOrder = hashMapOf(
                                        "quantity" to quant.toString(),
                                        "totalAmount" to total.toString(),
                                        "productId" to pId.toString(),
                                        "orderId" to orderId.toString(),
                                        "name" to name.toString(),
                                        "image" to img.toString(),
                                        "description" to desc.toString(),
                                        "Status" to "0",
                                        "buyerOrderIdAl" to buyerOrderIdAl
                                    )
                                    db.collection("seller")
                                        .document(sId.toString())
                                        .collection("sOrder")
                                        .document(orderId.toString())
                                        .set(sOrder)
                                        .addOnSuccessListener {

                                            Log.d("ORDER", "received 11")
                                        }

                                }
                                totalAmount1 + total > MinAmount -> {


                                    val order = hashMapOf(
                                        "totalAmount" to total.toString(),
                                        "quantity" to quant.toString(),
                                        "buyerAl" to buyerAl,
                                        "buyerOrderIdAl" to buyerOrderIdAl,
                                        "usable" to "0",
                                        "productId" to pId.toString(),
                                        "orderId" to orderId.toString(),
                                        "name" to name.toString(),
                                        "image" to img.toString(),
                                        "description" to desc.toString()
                                    )

                                    db.collection("orders")
                                        .document(sId.toString())
                                        .collection("mOrders")
                                        .document(orderId.toString())
                                        .set(order)
                                        .addOnSuccessListener {
                                            Log.d("ORDER","received 2")

                                            db.collection("orders")
                                                .document(sId.toString())
                                                .collection("mOrders")
                                                .get()
                                                .addOnSuccessListener { mainDo ->

                                                    mainDo.forEach { document ->

                                                        Log.d("hashmap set",document["name"].toString())

                                                        val sOrder = hashMapOf(
                                                            "quantity" to document["quantity"].toString(),
                                                            "totalAmount" to document["totalAmount"].toString(),
                                                            "productId" to document["productId"].toString(),
                                                            "name" to document["name"].toString(),
                                                            "image" to document["image"].toString(),
                                                            "description" to document["description"].toString(),
                                                            "Status" to "0",
                                                            "orderId" to document["orderId"].toString(),
                                                            "buyerOrderIdAl" to document["buyerOrderIdAl"]
                                                        )
                                                        db.collection("seller")
                                                            .document(sId.toString())
                                                            .collection("sOrder")
                                                            .document(document["orderId"].toString())
                                                            .set(sOrder)
                                                            .addOnSuccessListener {
                                                                Log.d("ORDER","received 22")
                                                            }

                                                        db.collection("orders")
                                                            .document(sId.toString())
                                                            .collection("mOrders")
                                                            .document(document["orderId"].toString())
                                                            .update("usable","0")
                                                    }

                                                }
                                        }
                                    //place seller order


                                }
                                else -> {
                                    val order = hashMapOf(
                                        "totalAmount" to total.toString(),
                                        "quantity" to quant.toString(),
                                        "buyerAl" to buyerAl,
                                        "buyerOrderIdAl" to buyerOrderIdAl,
                                        "usable"   to "1",
                                        "productId" to pId.toString(),
                                        "orderId" to orderId.toString(),
                                        "name" to name.toString(),
                                        "image" to img.toString(),
                                        "description" to desc.toString()
                                    )
                                    db.collection("orders")
                                        .document(sId.toString())
                                        .collection("mOrders")
                                        .document(orderId.toString())
                                        .set(order)
                                        .addOnSuccessListener {
                                      Log.d("ORDER", "received 3")

                                        }


                                }
                            }

                        }

                    }//SL1
                    .addOnFailureListener {

                        Log.d("fail", it.message.toString())
                    }

                val intent = Intent(this,Buyer_orders::class.java)
                startActivity(intent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { _, _ ->

            }
            dialog.setView(layout)
            dialog.show()
        }

    }

}