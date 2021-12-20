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
    var Status: String = ""


):Parcelable

/*
val sellerOrder = hashMapOf(
    "Name" to i["Name"],
    "Quantity" to i["QuantityFulfilled"].toString(),
    "TotalAmount" to (i["DiscountedPrice" +
            "" +
            ""].toString()
        .toInt() * i["QuantityFulfilled"].toString()
        .toInt()).toString(),
    "OrderId" to i["ProductId"].toString(),
    "Image" to i["Image"].toString(),
    "Description" to i["Description"].toString()

)*/
