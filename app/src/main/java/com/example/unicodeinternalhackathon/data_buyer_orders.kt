package com.example.unicodeinternalhackathon

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class data_buyer_orders(
    var quantity: String = "",
    var totalAmount: String = "",
    var sellerId: String = "",
    var productId: String = "",
    var name: String = "",
    var image: String = "",
    var description: String = "",
    var Status: String = "0",
    var userOrderId: String = "",
    var orderId: String = "",
    var discountedPrice: String = "",
    var mrp: String= "",
    var minAmount: String= "",
    var shop_name :String = ""
) : Parcelable