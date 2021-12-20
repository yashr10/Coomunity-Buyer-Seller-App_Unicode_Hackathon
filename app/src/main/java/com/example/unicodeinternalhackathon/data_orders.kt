package com.example.unicodeinternalhackathon

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class data_orders(
 var ProductId :String = "",
 var SellerId : String= "",
 var BuyerID : String = "",
 var Quantity : String = "",
 var PICost : String = "",
 var TotalAmount : String = "",
 var Image:String = "",
 var Name :String = "",
 var Description : String = "",
 var Status: String = "0",
 var Order_id:String = ""
):Parcelable
