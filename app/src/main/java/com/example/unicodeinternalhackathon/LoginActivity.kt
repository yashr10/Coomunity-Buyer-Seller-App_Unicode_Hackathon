package com.example.unicodeinternalhackathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

                                val intent = Intent(this@LoginActivity,
                                    SellerAddProduct::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}