package com.example.unicodeinternalhackathon

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class data_seller_order(

    var name : String = "",
    var quantity : String = "",
    var totalAmount : String = "",
    var orderId : String = "",
    var image : String = "",
    var description : String = "",
    var Status: String = "0",
    var productId : String = ""


):Parcelable


