package com.kleverowl.staffapp.orders.presenter

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.models.DeliveryModel
import com.kleverowl.staffapp.models.StaffModel
import com.kleverowl.staffapp.utils.OrderStatus
import com.kleverowl.staffapp.utils.PreferenceConnector
import com.kleverowl.staffapp.utils.StaffType
import com.kleverowl.staffapp.utils.Utils

class OrderInteractorImplementation : ListOrdersInteractor {

    override fun delivered(item: OrderModel) {
        val reference = FirebaseDatabase.getInstance().getReference("orders_table")
        val query = reference.orderByChild("orderId").equalTo(item.orderId)
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                orderFinishedListener.onError("No Order Found")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var found = false
                var dataSnapshot: DataSnapshot? = null
                if (p0.hasChildren()) {
                    for (d in p0.children) {
                        val data = d.getValue(OrderModel::class.java)
                        if (data?.orderId == item.orderId) {
                            dataSnapshot = d
                            found = true
                        }
                    }

                    if (found && dataSnapshot != null) {
                        item.orderStatus = OrderStatus.DELIVERED
                        item.deliveryDateTime = System.currentTimeMillis()
                        item.deliveryPhoneNumber = contactPersonInfo
                        item.orderCompleted = "1"
                        dataSnapshot.ref.setValue(item)
                        orderFinishedListener.onDelivered("Order Delivered Successfully")
                    } else {
                        orderFinishedListener.onError("No Order Found")
                    }

                } else {
                    orderFinishedListener.onError("No Order Found")
                }
            }
        }

        query.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun orderPickup(item: OrderModel) {
        val reference = FirebaseDatabase.getInstance().getReference("orders_table")
        val query = reference.orderByChild("orderId").equalTo(item.orderId)
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                orderFinishedListener.onError("No Order Found")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var found = false
                var dataSnapshot: DataSnapshot? = null
                if (p0.hasChildren()) {
                    for (d in p0.children) {
                        val data = d.getValue(OrderModel::class.java)
                        if (data?.orderId == item.orderId) {
                            dataSnapshot = d
                            found = true
                        }
                    }

                    if (found && dataSnapshot != null) {
                        item.orderStatus = OrderStatus.PICKED_UP
                        item.billedDate = System.currentTimeMillis()
                        item.deliveryPhoneNumber = contactPersonInfo
                        dataSnapshot.ref.setValue(item)
                        orderFinishedListener.onOrderPickedUp("Order Picked Up Successfully")
                    } else {
                        orderFinishedListener.onError("No Order Found")
                    }

                } else {
                    orderFinishedListener.onError("No Order Found")
                }
            }
        }

        query.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun readyToDispatch(item: OrderModel) {
        val reference = FirebaseDatabase.getInstance().getReference("orders_table")
        val query = reference.orderByChild("orderId").equalTo(item.orderId)
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                orderFinishedListener.onError("No Order Found")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var found = false
                var dataSnapshot: DataSnapshot? = null
                if (p0.hasChildren()) {
                    for (d in p0.children) {
                        val data = d.getValue(OrderModel::class.java)
                        if (data!!.orderId == item.orderId) {
                            dataSnapshot = d
                            found = true
                        }
                    }

                    if (found && dataSnapshot != null) {
                        item.orderStatus = OrderStatus.DISPATCHED
                        item.staffPhoneNumber = contactPersonInfo
                        item.readyToDispatchDate = System.currentTimeMillis()
                        dataSnapshot.ref.setValue(item)
                        orderFinishedListener.onOrderDispatched("Order Dispatched Successfully")
                    } else {
                        orderFinishedListener.onError("No Order Found")
                    }

                } else {
                    orderFinishedListener.onError("No Order Found")
                }
            }
        }

        query.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun processOrder(item: OrderModel) {
        val reference = FirebaseDatabase.getInstance().getReference("orders_table")
        val query = reference.orderByChild("orderId").equalTo(item.orderId)
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                orderFinishedListener.onError("No Order Found")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var found = false
                var dataSnapshot: DataSnapshot? = null
                if (p0.hasChildren()) {
                    for (d in p0.children) {
                        val data = d.getValue(OrderModel::class.java)
                        if (data!!.orderId == item.orderId) {
                            dataSnapshot = d
                            found = true
                        }
                    }

                    if (found && dataSnapshot != null) {
                        item.orderStatus = OrderStatus.IN_PROCESS
                        item.staffPhoneNumber = contactPersonInfo
                        item.processedOrderDate = System.currentTimeMillis()
                        dataSnapshot.ref.setValue(item)
                        orderFinishedListener.onOrderProcessed("Order Processed Successfully")
                    } else {
                        orderFinishedListener.onError("No Order Found")
                    }

                } else {
                    orderFinishedListener.onError("No Order Found")
                }
            }
        }

        query.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun setDetails(
        context: Context,
        ordersInteractorListener: ListOrdersInteractor.OrdersInteractorListener
    ) {
        this.mContext = context
        this.orderFinishedListener = ordersInteractorListener
        val type =
            PreferenceConnector.readInteger(mContext, PreferenceConnector.TYPE, StaffType.STAFF)

        contactPersonInfo = "NA"
        if (type == StaffType.STAFF) {
            val staffModel =
                Gson().fromJson(
                    PreferenceConnector.readString(mContext, PreferenceConnector.USER_DETAILS, ""),
                    StaffModel::class.java
                )
            contactPersonInfo = staffModel.name
        } else if (type == StaffType.DELIVERY) {
            val deliveryModel =
                Gson().fromJson(
                    PreferenceConnector.readString(mContext, PreferenceConnector.USER_DETAILS, ""),
                    DeliveryModel::class.java
                )
            contactPersonInfo = deliveryModel.name
        }


    }

    override fun getOrders() {
        val reference = FirebaseDatabase.getInstance().getReference("orders_table")
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = ArrayList<OrderModel>()
                if (p0.hasChildren()) {
                    for (d in p0.children) {
                        val data = d.getValue(OrderModel::class.java)
                        list.add(data!!)
                    }
                    list.reverse()
                    orderFinishedListener.onOrdersLoaded(list)
                } else {
                    orderFinishedListener.onError("No Orders Yet")
                }
            }

        }

        reference.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun getNewOrders() {

        var orderType = OrderStatus.ACCEPTED
        if (Utils.getStaffType(this.mContext) == StaffType.DELIVERY) {
            orderType = OrderStatus.DISPATCHED
        }

        val reference = FirebaseDatabase.getInstance().getReference("orders_table")
        val query = reference.orderByChild("orderStatus").equalTo(orderType)
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val list = ArrayList<OrderModel>()
                if (p0.hasChildren()) {
                    for (d in p0.children) {
                        val data = d.getValue(OrderModel::class.java)
                        if (data?.orderStatus == orderType)
                            list.add(data)
                    }

                    if (Utils.getStaffType(mContext) == StaffType.DELIVERY) {
                        list.sortByDescending {
                            it.readyToDispatchDate
                        }
                    } else {
                        list.sortByDescending {
                            it.acceptedOrderDate
                        }
                    }

                    orderFinishedListener.onOrdersLoaded(list)
                } else {
                    orderFinishedListener.onError("No Orders Yet")
                }
            }

        }

        query.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun getOnGoingOrders() {


        if (Utils.getStaffType(this.mContext) == StaffType.DELIVERY) {
            val orderType = OrderStatus.PICKED_UP

            val reference = FirebaseDatabase.getInstance().getReference("orders_table")
            val query = reference.orderByChild("orderStatus").equalTo(orderType)
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val list = ArrayList<OrderModel>()
                    if (p0.hasChildren()) {
                        for (d in p0.children) {
                            val data = d.getValue(OrderModel::class.java)
                            if (data?.deliveryPhoneNumber.equals(contactPersonInfo)) {
                                list.add(data!!)
                            }
                        }

                        list.sortByDescending {
                            it.billedDate
                        }

                        orderFinishedListener.onOrdersLoaded(list)
                    } else {
                        orderFinishedListener.onError("No Orders Yet")
                    }
                }

            }

            query.addListenerForSingleValueEvent(valueEventListener)

        } else {
            val orderType = OrderStatus.IN_PROCESS
            val reference = FirebaseDatabase.getInstance().getReference("orders_table")
            val query = reference.orderByChild("orderStatus").equalTo(orderType)
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val list = ArrayList<OrderModel>()
                    if (p0.hasChildren()) {
                        for (d in p0.children) {
                            val data = d.getValue(OrderModel::class.java)
                            if (data?.staffPhoneNumber.equals(contactPersonInfo)) {
                                list.add(data!!)
                            }
                        }
                        list.sortByDescending {
                            it.processedOrderDate
                        }
                        orderFinishedListener.onOrdersLoaded(list)
                    } else {
                        orderFinishedListener.onError("No Orders Yet")
                    }
                }

            }

            query.addListenerForSingleValueEvent(valueEventListener)
        }


    }

    override fun getPastOrders() {


        if (Utils.getStaffType(this.mContext) == StaffType.DELIVERY) {
            val orderType = OrderStatus.DELIVERED
            val reference = FirebaseDatabase.getInstance().getReference("orders_table")
            val query = reference.orderByChild("orderStatus").equalTo(orderType)
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val list = ArrayList<OrderModel>()
                    if (p0.hasChildren()) {
                        for (d in p0.children) {
                            val data = d.getValue(OrderModel::class.java)
                            if (data?.deliveryPhoneNumber.equals(contactPersonInfo)) {
                                if (data?.orderStatus == OrderStatus.DELIVERED) {
                                    list.add(data)
                                }
                            }
                        }
                        list.sortByDescending {
                            it.deliveryDateTime
                        }
                        orderFinishedListener.onOrdersLoaded(list)
                    } else {
                        orderFinishedListener.onError("No Orders Yet")
                    }
                }

            }

            query.addListenerForSingleValueEvent(valueEventListener)
        } else {
            val reference = FirebaseDatabase.getInstance().getReference("orders_table")
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val list = ArrayList<OrderModel>()
                    if (p0.hasChildren()) {
                        for (d in p0.children) {
                            val data = d.getValue(OrderModel::class.java)
                            if (data?.staffPhoneNumber.equals(contactPersonInfo)) {
                                if (data?.orderStatus == OrderStatus.DELIVERED || data?.orderStatus == OrderStatus.DISPATCHED) {
                                    list.add(data)
                                }
                            }
                        }
                        list.sortByDescending {
                            it.readyToDispatchDate
                        }
                        orderFinishedListener.onOrdersLoaded(list)
                    } else {
                        orderFinishedListener.onError("No Orders Yet")
                    }
                }

            }

            reference.addListenerForSingleValueEvent(valueEventListener)
        }
    }


    private lateinit var contactPersonInfo: String
    private lateinit var orderFinishedListener: ListOrdersInteractor.OrdersInteractorListener
    private lateinit var mContext: Context

}