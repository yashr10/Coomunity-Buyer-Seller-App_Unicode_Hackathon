package com.example.unicodeinternalhackathon

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_phone_login_register.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit

class PhoneLoginRegisterActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private val db = Firebase.firestore

    lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_login_register)

        mAuth = FirebaseAuth.getInstance()
        var isBuyer: Boolean? = true

        ph_bt_buyer.setOnClickListener {
            ph_bt_buyer.background = AppCompatResources.getDrawable(this,
                R.drawable.register_bs_button_clicked)
            ph_bt_seller.background = AppCompatResources.getDrawable(this,
                R.drawable.register_bs_button_unclicked)
            isBuyer = true
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
                R.drawable.register_bs_button_clicked)
            ph_bt_buyer.background = AppCompatResources.getDrawable(this,
                R.drawable.register_bs_button_unclicked)
            isBuyer = false
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
                    val sPhNo: String = ph_et_sPhNo_register.text.toString().trim { it <= ' ' }


                    val options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+sPhNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)

                    ph_et_bName_register.visibility = View.GONE
                    ph_et_bAddress_register.visibility = View.GONE
                    ph_et_bRegNo_register.visibility = View.GONE
                    ph_et_bPhNo_register.visibility = View.GONE
                    ph_et_bEmail_register.visibility = View.GONE
                    ph_bt_bSendOTP_register.visibility = View.GONE

                    ph_et_shopName_register.visibility = View.GONE
                    ph_et_sAddress_register.visibility = View.GONE
                    ph_et_sPanNo_register.visibility = View.GONE
                    ph_et_sPhNo_register.visibility = View.GONE
                    ph_et_sEmail_register.visibility = View.GONE
                    ph_bt_sSendOTP_register.visibility = View.GONE

                    ph_et_OTP_register.visibility = View.VISIBLE
                    ph_tv_resend_otp.visibility = View.VISIBLE
                    ph_bt_verify.visibility =View.VISIBLE
                }
            }
        }


        ph_bt_bSendOTP_register.setOnClickListener {
            when {
                TextUtils.isEmpty(ph_et_bName_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Shop Name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(ph_et_bAddress_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Address.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(ph_et_bRegNo_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Pan Card Number.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(ph_et_bPhNo_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Phone Number.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(ph_et_bEmail_register.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please Enter Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val bName: String = ph_et_bName_register.text.toString().trim { it <= ' ' }
                    val bAddress: String = ph_et_bAddress_register.text.toString().trim { it <= ' ' }
                    val bRegNo: String = ph_et_bRegNo_register.text.toString().trim { it <= ' ' }
                    val bPhNo: String = ph_et_bPhNo_register.text.toString().trim { it <= ' ' }
                    val bEmail: String = ph_et_bEmail_register.text.toString().trim { it <= ' ' }


                    val options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+bPhNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)

                    ph_et_bName_register.visibility = View.GONE
                    ph_et_bAddress_register.visibility = View.GONE
                    ph_et_bRegNo_register.visibility = View.GONE
                    ph_et_bPhNo_register.visibility = View.GONE
                    ph_et_bEmail_register.visibility = View.GONE
                    ph_bt_bSendOTP_register.visibility = View.GONE

                    ph_et_shopName_register.visibility = View.GONE
                    ph_et_sAddress_register.visibility = View.GONE
                    ph_et_sPanNo_register.visibility = View.GONE
                    ph_et_sPhNo_register.visibility = View.GONE
                    ph_et_sEmail_register.visibility = View.GONE
                    ph_bt_sSendOTP_register.visibility = View.GONE

                    ph_et_OTP_register.visibility = View.VISIBLE
                    ph_tv_resend_otp.visibility = View.VISIBLE
                    ph_bt_verify.visibility =View.VISIBLE
                }
            }
        }

        ph_bt_verify.setOnClickListener {
            val otp=ph_et_OTP_register.text.toString().trim()
            if(otp.isNotEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
                signInWithPhoneAuthCredential(credential, isBuyer!!)
            }else{
                Toast.makeText(this,"Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential, isBuyer!!)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

                Toast.makeText(
                    this@PhoneLoginRegisterActivity,
                    "OTP Sent",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
    }

    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, isBuyer: Boolean) {

        val shopName: String = ph_et_shopName_register.text.toString().trim { it <= ' ' }
        val sAddress: String = ph_et_sAddress_register.text.toString().trim { it <= ' ' }
        val sPanNo: String = ph_et_sPanNo_register.text.toString().trim { it <= ' ' }
        val sPhNo: String = ph_et_sPhNo_register.text.toString().trim { it <= ' ' }
        val sEmail: String = ph_et_sEmail_register.text.toString().trim { it <= ' ' }


        val bName: String = ph_et_bName_register.text.toString().trim { it <= ' ' }
        val bAddress: String = ph_et_bAddress_register.text.toString().trim { it <= ' ' }
        val bRegNo: String = ph_et_bRegNo_register.text.toString().trim { it <= ' ' }
        val bPhNo: String = ph_et_bPhNo_register.text.toString().trim { it <= ' ' }
        val bEmail: String = ph_et_bEmail_register.text.toString().trim { it <= ' ' }

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    Toast.makeText(
                        this,
                        "You were registered successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    if(isBuyer){
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
                    }else{
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

                        startActivity(Intent(this,SellerProducts::class.java))
                        finish()
                    }


                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun updateUI(user: FirebaseUser? = mAuth.currentUser) {

    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
    }
}