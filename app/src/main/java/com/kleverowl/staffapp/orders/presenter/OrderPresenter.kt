package com.kleverowl.staffapp.orders.presenter

import android.content.Context
import com.kleverowl.staffapp.models.OrderModel

interface OrderPresenter {
    fun setDetails(context :Context)
    fun onResume()
    fun onDestroy()
    fun processOrder(item: OrderModel)
    fun readyToDispatch(item: OrderModel)
    fun getNewOrders()
    fun orderPickup(item: OrderModel)
    fun getOnGoingOrders()
    fun delivered(item: OrderModel)
    fun getPastOrders()
}