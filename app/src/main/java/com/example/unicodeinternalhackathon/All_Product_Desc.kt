package com.example.unicodeinternalhackathon

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
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import kotlin.collections.ArrayList

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
    //    val qf = intent.extras!!.getString("qf")
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
    //    tvQf.text = qf

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
                val total: Int = quant*dp.toString().toInt()


        //        tvQf.text = total.toString()

                val orderId = UUID.randomUUID()
                val userOrderId = UUID.randomUUID()

                db.collection("orders")
                    .document(sId.toString())
                    .collection("mOrders")
                    .get()
                    .addOnSuccessListener { mainDoc -> //SL1

                        Log.d("listner","success")
                        var totalAmount1 = 0
                        var isSame  = false
                        var minQuant = false
                        var sameId = ""

                      mainDoc.forEach { document ->


                          if(document["usable"].toString()=="1"){
                              totalAmount1 += document["totalAmount"].toString().toInt()
                          }


                          if (document["productId"].toString() == pId.toString() && document["usable"].toString()=="1"){

                              isSame = true
                              sameId = document["orderId"].toString()
                           val totalAmount = document["totalAmount"].toString().toInt() + total // total amount of the product order you are placing
                            val  quantity = document["quantity"].toString().toInt() + quant

                              Log.d("for each if",totalAmount.toString())
                              Log.d("for each if",quantity.toString())

                              db.collection("orders")
                                  .document(sId.toString())
                                  .collection("mOrders")
                                  .document(document["orderId"].toString())
                                  .update(mapOf(
                                      "totalAmount" to totalAmount.toString(),
                                      "quantity" to quantity.toString(),
                                      "buyerAl" to FieldValue.arrayUnion(mAuth.currentUser!!.uid),
                                      "buyerOrderIdAl" to FieldValue.arrayUnion(userOrderId.toString())
                                  ))

                              if(quantity >= min.toString().toInt()){
                                  Log.d("for each if inside","quantity >")
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
                                      "minAmount"  to MinAmount.toString()

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
                                      .update("usable","0")



                              } //quantity >= min.toString().toInt()


                          }  // if khatam



                      }
                        if (isSame && !minQuant){

                            val t = total + totalAmount1
                            Log.d("same total equla",t.toString())

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
                                "minAmount"  to MinAmount.toString()

                            )

                            db.collection("buyer")
                                .document(mAuth.currentUser!!.uid)
                                .collection("bOrders")
                                .document(userOrderId.toString())
                                .set(bOrder)
                                .addOnSuccessListener {

                                }
                            if (t>MinAmount){

                                Log.d("t>minamo",t.toString())

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
                                                    "orderId" to document["orderId"].toString()
                                                )
                                                db.collection("seller")
                                                    .document(sId.toString())
                                                    .collection("sOrder")
                                                    .document(document["orderId"].toString())
                                                    .set(sOrder)
                                                    .addOnSuccessListener {
                                                        Log.d("sOrder","placed")
                                                    }.addOnFailureListener {
                                                        Log.d("sOrder",it.message.toString())
                                                    }

                                            db.collection("orders")
                                                .document(sId.toString())
                                                .collection("mOrders")
                                                .document(document["orderId"].toString())
                                                .update("usable","0")
                                        }

                                    }





                            }


                        }
                        if (!isSame){

                            //place buyer order

                                Log.d("reached","'here")
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
                                "minAmount"  to MinAmount.toString()

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
                                        "Status" to "0"
                                    )
                                    db.collection("seller")
                                        .document(sId.toString())
                                        .collection("sOrder")
                                        .document(orderId.toString())
                                        .set(sOrder)
                                        .addOnSuccessListener {
                                            Log.d("ORDER","received 11")
                                        }

                                }
                                totalAmount1+total > MinAmount -> {

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
                                                            "orderId" to document["orderId"].toString()
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
                                            Log.d("ORDER","received 3")
                                        }


                                }
                            }

                        }

                    }//SL1
                    .addOnFailureListener {

                        Log.d("fail",it.message.toString())
                    }












                /*//storing data to seller collection in firebase as per the requirement
                db.collection("seller")
                    .document(sId.toString())
                    .collection("products")
                    .document(pId.toString())
                    .update("QuantityFulfilled", total.toString())
                    .addOnSuccessListener {
                        val totalAmount = dp.toString().toInt() * total
                        val id = UUID.randomUUID().toString()
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
                            "Status" to "0",
                            "Order_Id" to id
                        )


                        db.collection("buyer")
                            .document(mAuth.currentUser!!.uid)
                            .collection("orders")
                            .document(id)
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
                    }*/
            }
            dialog.setNegativeButton("Cancel") { _, _ ->

            }
            dialog.setView(layout)
            dialog.show()
        }

    }

}