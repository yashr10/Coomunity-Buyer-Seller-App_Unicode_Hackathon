package com.example.unicodeinternalhackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    private val db = Firebase.firestore
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //initialising firebase auth
        mAuth = FirebaseAuth.getInstance()

        tv_registerNow_login.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

        bt_login_login.setOnClickListener {
            when {
                TextUtils.isEmpty(et_email_login.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@LoginActivity,
                        "Please Enter Email.",
                        Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(et_password_login.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@LoginActivity,
                        "Please Enter Password.",
                        Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val email: String = et_email_login.text.toString().trim { it <= ' ' }
                    val password: String = et_password_login.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                task.result!!.user!!
                                Toast.makeText(this@LoginActivity,
                                    "You were logged in successfully",
                                    Toast.LENGTH_SHORT).show()


                                db.collection("buyer")
                                    .get()
                                    .addOnSuccessListener { result ->
                                        for (document in result) {
                                            if(document["user_id"].toString() == task.result!!.user!!.uid){
                                                startActivity(Intent(this,Buyer_All_Products::class.java))
                                                finish()
                                            }
                                        }

                                    }
                                    .addOnFailureListener { exception ->
                                        Toast.makeText(this@LoginActivity,
                                            "Failure",
                                            Toast.LENGTH_SHORT).show()
                                    }

                                db.collection("seller")
                                    .get()
                                    .addOnSuccessListener { result ->
                                        for (document in result) {
                                            if(document["user_id"].toString() == task.result!!.user!!.uid){
                                                startActivity(Intent(this,SellerProducts::class.java))
                                                finish()
                                            }
                                        }

                                    }
                                    .addOnFailureListener { exception ->
                                        Toast.makeText(this@LoginActivity,
                                            "Failure",
                                            Toast.LENGTH_SHORT).show()
                                    }

                            } else {
                                Toast.makeText(this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }

        tv_phoneNumber_login.setOnClickListener {
            startActivity(Intent(this,PhoneLoginRegisterActivity::class.java))
            finish()
        }
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        //redirect to main Activity if user is not null
        if (currentUser != null){
            db.collection("buyer")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(document["user_id"].toString() == currentUser.uid){
                            startActivity(Intent(this,Buyer_All_Products::class.java))
                            finish()
                        }
                    }

                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this@LoginActivity,
                        "Failure",
                        Toast.LENGTH_SHORT).show()
                }

            db.collection("seller")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if(document["user_id"].toString() == currentUser.uid){
                            startActivity(Intent(this,SellerProducts::class.java))
                            finish()
                        }
                    }

                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this@LoginActivity,
                        "Failure",
                        Toast.LENGTH_SHORT).show()
                }
        }
    }
}