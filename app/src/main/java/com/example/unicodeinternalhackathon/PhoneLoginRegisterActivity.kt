package com.example.unicodeinternalhackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_phone_login_register.*
import kotlinx.android.synthetic.main.activity_register.*

class PhoneLoginRegisterActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_login_register)

        mAuth = FirebaseAuth.getInstance()

        ph_bt_buyer.setOnClickListener {
            ph_bt_buyer.background = AppCompatResources.getDrawable(this,R.drawable.back_button_clicked)
            ph_bt_seller.background = AppCompatResources.getDrawable(this,R.drawable.back_button)
            ph_et_bName_register.visibility = View.VISIBLE
            ph_et_bAddress_register.visibility = View.VISIBLE
            ph_et_bRegNo_register.visibility = View.VISIBLE
            ph_et_bPhNo_register.visibility = View.VISIBLE
            ph_et_bEmail_register.visibility = View.VISIBLE
            ph_bt_bSendOTP_register.visibility = View.VISIBLE


            ph_et_shopName_register.visibility = View.GONE
            ph_et_sAddress_register.visibility = View.GONE
            ph_et_sPanNo_register.visibility = View.GONE
            ph_et_sPhNo_register.visibility = View.GONE
            ph_et_sEmail_register.visibility = View.GONE
            ph_bt_sSendOTP_register.visibility = View.GONE
        }
        ph_bt_seller.setOnClickListener {
            ph_bt_seller.background = AppCompatResources.getDrawable(this,
                R.drawable.back_button_clicked)
            ph_bt_buyer.background = AppCompatResources.getDrawable(this,
                R.drawable.back_button)
            ph_et_shopName_register.visibility = View.VISIBLE
            ph_et_sAddress_register.visibility = View.VISIBLE
            ph_et_sPanNo_register.visibility = View.VISIBLE
            ph_et_sPhNo_register.visibility = View.VISIBLE
            ph_et_sEmail_register.visibility = View.VISIBLE
            ph_bt_sSendOTP_register.visibility = View.VISIBLE


            ph_et_bName_register.visibility = View.GONE
            ph_et_bAddress_register.visibility = View.GONE
            ph_et_bRegNo_register.visibility = View.GONE
            ph_et_bPhNo_register.visibility = View.GONE
            ph_et_bEmail_register.visibility = View.GONE
            ph_bt_bSendOTP_register.visibility = View.GONE
        }

        ph_tv_already_user.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        ph_bt_sSendOTP_register.setOnClickListener {
            when {
                TextUtils.isEmpty(ph_et_shopName_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Shop Name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(ph_et_sAddress_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Address.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(ph_et_sPanNo_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Pan Card Number.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(ph_et_sPhNo_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Phone Number.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(ph_et_sEmail_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val shopName: String = ph_et_shopName_register.text.toString().trim { it <= ' ' }
                    val sAddress: String = ph_et_sAddress_register.text.toString().trim { it <= ' ' }
                    val sPanNo: String = ph_et_sPanNo_register.text.toString().trim { it <= ' ' }
                    val sPhNo: String = ph_et_sPhNo_register.text.toString().trim { it <= ' ' }
                    val sEmail: String = ph_et_sEmail_register.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(sEmail, sPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                task.result!!.user!!
                                Toast.makeText(
                                    this,
                                    "You were registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val user = hashMapOf(

                                    "shop_name" to shopName,
                                    "email" to sEmail,
                                    "pan_card_number" to sPanNo,
                                    "phone_number" to sPhNo,
                                    "address" to sAddress,
                                    "user_id" to task.result!!.user!!.uid
                                )

                                db.collection("seller")
                                    .document(task.result!!.user!!.uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        Log.d("data in Firestore" , "true")
                                    }
                                    .addOnFailureListener {
                                        Log.d("data in Firestore",it.message.toString() )
                                    }

                                startActivity(Intent(this,SellerOrders::class.java))
                                finish()

                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }


        bt_bRegister_register.setOnClickListener {
            when {
                TextUtils.isEmpty(et_bName_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Shop Name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_bAddress_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Address.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_bRegNo_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Pan Card Number.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_bPhNo_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Phone Number.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_bEmail_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_bPassword_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val bName: String = et_bName_register.text.toString().trim { it <= ' ' }
                    val bAddress: String = et_bAddress_register.text.toString().trim { it <= ' ' }
                    val bRegNo: String = et_bRegNo_register.text.toString().trim { it <= ' ' }
                    val bPhNo: String = et_bPhNo_register.text.toString().trim { it <= ' ' }
                    val bEmail: String = et_bEmail_register.text.toString().trim { it <= ' ' }
                    val bPassword: String = et_bPassword_register.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(bEmail, bPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                task.result!!.user!!
                                Toast.makeText(
                                    this,
                                    "You were registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val user = hashMapOf(

                                    "Name" to bName,
                                    "email" to bEmail,
                                    "pan_card_number" to bRegNo,
                                    "phone_number" to bPhNo,
                                    "address" to bAddress,
                                    "user_id" to task.result!!.user!!.uid
                                )

                                db.collection("buyer")
                                    .document(task.result!!.user!!.uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        Log.d("data in Firestore" , "true")
                                    }
                                    .addOnFailureListener {
                                        Log.d("data in Firestore",it.message.toString() )
                                    }

                                startActivity(Intent(this,Buyer_All_Products::class.java))
                                finish()

                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

    }
}