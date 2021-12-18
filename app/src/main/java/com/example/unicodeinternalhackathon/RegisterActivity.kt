package com.example.unicodeinternalhackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var isBuyer: Boolean? = null

        bt_buyer.setOnClickListener {
            bt_buyer.background = AppCompatResources.getDrawable(this,R.drawable.back_button_clicked)
            bt_seller.background = AppCompatResources.getDrawable(this,R.drawable.back_button)
            isBuyer = true
            et_bName_register.visibility = View.VISIBLE
            et_bAddress_register.visibility = View.VISIBLE
            et_bRegNo_register.visibility = View.VISIBLE
            et_bEmail_register.visibility = View.VISIBLE
            et_bPassword_register.visibility = View.VISIBLE
            bt_bRegister_register.visibility = View.VISIBLE


            et_shopName_register.visibility = View.GONE
            et_sAddress_register.visibility = View.GONE
            et_sPanNo_register.visibility = View.GONE
            et_sEmail_register.visibility = View.GONE
            et_sPassword_register.visibility = View.GONE
            bt_sRegister_register.visibility = View.GONE
        }
        bt_seller.setOnClickListener {
            bt_seller.background = AppCompatResources.getDrawable(this,R.drawable.back_button_clicked)
            bt_buyer.background = AppCompatResources.getDrawable(this,R.drawable.back_button)
            isBuyer = false
            et_shopName_register.visibility = View.VISIBLE
            et_sAddress_register.visibility = View.VISIBLE
            et_sPanNo_register.visibility = View.VISIBLE
            et_sEmail_register.visibility = View.VISIBLE
            et_sPassword_register.visibility = View.VISIBLE
            bt_sRegister_register.visibility = View.VISIBLE


            et_bName_register.visibility = View.GONE
            et_bAddress_register.visibility = View.GONE
            et_bRegNo_register.visibility = View.GONE
            et_bEmail_register.visibility = View.GONE
            et_bPassword_register.visibility = View.GONE
            bt_bRegister_register.visibility = View.GONE
        }

        tv_already_user.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}