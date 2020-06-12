package com.kleverowl.staffapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeliveryModel(
    var id: String = "",
    var name: String = "",
    var mobileNumber: String = "",
    var password: String = ""
) : Parcelable