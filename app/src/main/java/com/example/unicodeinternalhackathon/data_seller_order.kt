package com.example.unicodeinternalhackathon

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class data_seller_order(

    var Name : String = "",
    var Quantity : String = "",
    var TotalAmount : String = "",
    var OrderId : String = "",
    var Image : String = "",
    var Description : String = "",
    var Status: String = "0",
    var Order_id:String = ""


):Parcelable


