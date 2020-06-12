package com.kleverowl.staffapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StaffModel(
    var id: String = "",
    var name: String = "",
    var mobileNumber: String = "",
    var password: String = ""
) : Parcelable