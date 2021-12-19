package com.example.unicodeinternalhackathon

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class All_Product_Desc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_product_desc)

        //getting data fromrecyclerview using intent and assigning to the variables
        val name = intent.extras!!.getString("name")
        val img = intent.extras!!.getString("img")
        val desc = intent.extras!!.getString("desc")
        val dp = intent.extras!!.getString("dp")
        val min = intent.extras!!.getString("min")
        val mrp = intent.extras!!.getString("mrp")

        //setting findview by id to put value sin the fields of product details page
        val tvName = findViewById<TextView>(R.id.tv_all_products_name)
        val imgProd = findViewById<ImageView>(R.id.im_products_desc)
        val tvDesc = findViewById<TextView>(R.id.tv_product_desc_desc)
        val tvDp = findViewById<TextView>(R.id.tv_product_desc_dp)
        val tvMin = findViewById<TextView>(R.id.tv_product_desc_min)
        val tvMrp = findViewById<TextView>(R.id.tv_product_desc_mrp)

        //putting data in product details page
        tvName.text = name
        tvDesc.text = desc
        tvDp.text = dp
        tvMin.text = min
        tvMrp.text = mrp
        Glide.with(this)
            .load(img.toString())
            .into(imgProd)

        //button for adding requirement
        val req = findViewById<Button>(R.id.bt_prod_desc_req)
        req.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val inflater = LayoutInflater.from(applicationContext)
            val view = inflater.inflate(R.layout.dialog_input, null)
            dialog.setView(view)
            dialog.setTitle("Add requirement")
            dialog.setMessage("Enter your required quantity")
            dialog.setPositiveButton("Ok") { _, _ ->
                val input = view.findViewById<EditText>(R.id.et_dialog_input)
            }
            dialog.setNegativeButton("Cancel") { _, _ ->

            }
            dialog.create()
        }

    }
}