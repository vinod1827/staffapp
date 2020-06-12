package com.kleverowl.staffapp.orders.presenter

import android.content.Context
import com.kleverowl.staffapp.models.OrderModel

interface ListOrdersInteractor {

    interface OrdersInteractorListener {
        fun onOrdersLoaded(list: ArrayList<OrderModel>)
        fun onError(message: String)
        fun onOrderProcessed(message: String)
        fun onOrderDispatched(message: String)
        fun onOrderPickedUp(message: String)
        fun onDelivered(message: String)
    }

    fun setDetails(context: Context, ordersInteractorListener: OrdersInteractorListener)
    fun getOrders()

    fun getNewOrders()
    fun getOnGoingOrders()
    fun getPastOrders()

    fun processOrder(item: OrderModel)
    fun delivered(item: OrderModel)
    fun readyToDispatch(item: OrderModel)
    fun orderPickup(item: OrderModel)
}