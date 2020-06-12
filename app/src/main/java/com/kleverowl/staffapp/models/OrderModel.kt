package com.kleverowl.staffapp.models

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kleverowl.staffapp.utils.OrderStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "orders")
data class OrderModel(
    @NonNull
    @PrimaryKey
    var orderId: String = "",
    var customerId: String = "",
    var customerName: String = "",
    var date: Long? = null,
    var address: String = "",
    var contactPersonInfo: String = "",
    var deliveryBoyInfo: String = "",
    var orderStatus: String = "",
    var customerMobileNumber: String? = null,
    var companyName: String = "",
    var deliveryPhoneNumber: String? = null,
    var staffPhoneNumber: String? = null,
    var orderDateTime: Long? = null,
    var deliveryDateTime: Long? = null,
    var acceptedOrderDate: Long? = null,
    var cancelledOrderDate: Long? = null,
    var processedOrderDate: Long? = null,
    var readyToDispatchDate: Long? = null,
    var billedDate: Long? = null,
    var gstNumber: String? = "",
    var rating: Float? = 0F,
    var orderCompleted: String = "0",
    var orderProductModelList: ArrayList<OrderProductsModel>? = null
) : Parcelable