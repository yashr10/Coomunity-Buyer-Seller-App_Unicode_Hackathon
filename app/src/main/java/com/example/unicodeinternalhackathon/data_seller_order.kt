package com.example.unicodeinternalhackathon

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class data_seller_order(

<<<<<<< HEAD
    var Name : String = "",
    var Quantity : String = "",
    var TotalAmount : String = "",
    var OrderId : String = "",
    var Image : String = "",
    var Description : String = "",
    var Status: String = "0",
    var Order_id:String = ""
=======
    var name : String = "",
    var quantity : String = "",
    var totalAmount : String = "",
    var orderId : String = "",
    var image : String = "",
    var description : String = "",
    var Status: String = "0",
    var productId : String = ""
>>>>>>> 459d584a7372572470e63e91ddbd6f5489c61d9a


):Parcelable


