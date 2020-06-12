package com.kleverowl.staffapp.orders.views

import com.kleverowl.staffapp.models.OrderModel


interface ListOrdersView {
    fun showProgress()
    fun hideProgress()
    fun onOrderLoaded(list : ArrayList<OrderModel>)
    fun onError(message: String)
    fun orderProcessed(message: String)
    fun orderDispatched(message: String)
    fun orderPickedup(message: String)
    fun onDelivered(message: String)
}