package com.kleverowl.staffapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderProductsModel(
    var id: String? = null,
    var name: String = "",
    var productCode: String = "",
    var size: String = "",
    var thickness: String = "",
    var surface: String = "",
    var quantity: Int? = 0,
    var catalogueId: String = ""
) : Parcelable