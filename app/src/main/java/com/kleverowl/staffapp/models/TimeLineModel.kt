package com.kleverowl.staffapp.models

import android.os.Parcelable
import com.kleverowl.customerapp.models.OrderStatus
import kotlinx.android.parcel.Parcelize

/**
 * Created by Vipul Asri on 05-12-2015.
 */
@Parcelize
class TimeLineModel(
        var message: String,
        var status: OrderStatus
) : Parcelable
