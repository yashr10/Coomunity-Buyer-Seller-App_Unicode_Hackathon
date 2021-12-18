package com.example.unicodeinternalhackathon

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class data_all_products(
    var Description: String = "",
    var DiscountedPrice: String = "",
    var Image: String = "",
    var MRP : String = "",
    var MinQuantity:String = "",
    var Name:String = "",
    var ProductId :String = ""
) : Parcelable