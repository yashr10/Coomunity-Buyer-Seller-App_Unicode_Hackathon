package com.example.unicodeinternalhackathon

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class data_all_products(
    var Description: String = "",
    var DiscountedPrice: String = "",
    var Image: String = "",
    var MRP: String = "",
    var MinQuantity: String = "",
    var Name: String = "",
    var QuantityFulfilled:String = "",
    var ProductId:String = "",
    var SellerId:String = ""
):Parcelable


/*
val product = hashMapOf(
    "Name" to productName.text.toString(),
    "Description" to productDesc.text.toString(),
    "MRP" to productMrp.text.toString(),
    "DiscountedPrice" to productDiscountedPrice.text.toString(),
    "MinQuantity" to productMinQuantity.text.toString(),
    "Image" to imgUrl,
    "ProductId" to productId.toString(),
    "QuantityFulfilled" to "0",
    "SellerId" to mAuth.currentUser!!.uid
)
*/
