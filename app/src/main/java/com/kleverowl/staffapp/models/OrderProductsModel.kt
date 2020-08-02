package com.kleverowl.staffapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderProductsModel(
    var id: String? = null,
    var name: String = "",
    var productCode: String = "",
    var quantity: Int? = 0,
    var catalogueId: String = "",
    var price: String = ""
) : Parcelable