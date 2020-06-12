package com.kleverowl.staffapp.orders.presenter

import android.content.Context
import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.orders.views.ListOrdersView

class OrderPresenterImplementation(
    private var orderView: ListOrdersView?,
    private val orderInteractor: ListOrdersInteractor
) : OrderPresenter,
    ListOrdersInteractor.OrdersInteractorListener {
    override fun delivered(item: OrderModel) {
        if (orderView != null) {
            orderView?.showProgress()
            orderInteractor.delivered(item)
        }
    }

    override fun getPastOrders() {
        if (orderView != null) {
            orderView?.showProgress()
            orderInteractor.getPastOrders()
        }
    }

    override fun onDelivered(message: String) {
        if (orderView != null) {
            orderView?.hideProgress()
            orderView?.onDelivered(message)
        }
    }

    override fun getOnGoingOrders() {
        if (orderView != null) {
            orderView?.showProgress()
            orderInteractor.getOnGoingOrders()
        }
    }

    override fun onOrderPickedUp(message: String) {
        if (orderView != null) {
            orderView?.hideProgress()
            orderView?.orderPickedup(message)
        }
    }

    override fun orderPickup(item: OrderModel) {
        if (orderView != null) {
            orderView?.showProgress()
            orderInteractor.orderPickup(item)
        }
    }

    override fun getNewOrders() {
        if (orderView != null) {
            orderView?.showProgress()
            orderInteractor.getNewOrders()
        }
    }

    override fun onOrderDispatched(message: String) {
        if (orderView != null) {
            orderView?.hideProgress()
            orderView?.orderDispatched(message)
        }
    }

    override fun readyToDispatch(item: OrderModel) {
        if (orderView != null) {
            orderView?.showProgress()
            orderInteractor.readyToDispatch(item)
        }
    }

    override fun onOrderProcessed(message: String) {
        if (orderView != null) {
            orderView?.hideProgress()
            orderView?.orderProcessed(message)
        }
    }

    override fun processOrder(item: OrderModel) {
        if (orderView != null) {
            orderView?.showProgress()
            orderInteractor.processOrder(item)
        }
    }

    override fun onOrdersLoaded(list: ArrayList<OrderModel>) {
        if (orderView != null) {
            orderView?.hideProgress()
            orderView?.onOrderLoaded(list)
        }
    }

    override fun setDetails(context: Context) {
        if (orderView != null) {
            orderInteractor.setDetails(context, this)
        }
    }

    override fun onResume() {
        if (orderView != null) {
            orderView?.showProgress()
            orderInteractor.getOrders()
        }
    }

    override fun onDestroy() {
        orderView = null
    }

    override fun onError(message: String) {
        if (orderView != null) {
            orderView?.hideProgress()
            orderView?.onError(message)
        }
    }

}