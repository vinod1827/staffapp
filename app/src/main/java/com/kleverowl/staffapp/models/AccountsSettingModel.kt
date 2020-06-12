package com.kleverowl.staffapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class AccountsSettingModel(
    val id: String,
    val title: String,
    val drawable: Int,
    val isEnabled: Boolean? = false
) : Parcelable