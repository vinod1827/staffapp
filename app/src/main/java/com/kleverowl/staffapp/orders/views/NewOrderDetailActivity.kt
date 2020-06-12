package com.kleverowl.staffapp.orders.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_new_order_detail.*
import kotlinx.android.synthetic.main.activity_new_order_detail.manageProductsRecyclerView
import kotlinx.android.synthetic.main.toolbar.*
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.adapters.OrderProductRecyclerViewAdapter
import com.kleverowl.staffapp.components.LoadingProgressBar
import com.kleverowl.staffapp.models.OrderProductsModel
import com.kleverowl.staffapp.utils.AppConstants
import com.kleverowl.staffapp.utils.OrderStatus
import kotlinx.android.synthetic.main.confirmation_dialog.view.*


class NewOrderDetailActivity : AppCompatActivity() {

    private lateinit var loadingProgressBar: LoadingProgressBar
    private lateinit var updatedList: java.util.ArrayList<OrderProductsModel>
    private var orderModel: OrderModel? = null
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_new_order_detail)
        loadingProgressBar = LoadingProgressBar()
        setSupportActionBar(toolbar)


        orderModel = intent?.extras?.getParcelable(AppConstants.ORDER_MODEL)

        supportActionBar?.title = "New Order"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_product)
        toolbarTitle.text = ""
        toolbarTitle.visibility = View.GONE

        initProductItemsRecyclerView()

        setDataListItems()

        acceptOrderButton.setOnClickListener {
            showConfirmationDialog("Do you want to Accept this order?", 1)
        }

        cancelOrderButton.setOnClickListener {
            showConfirmationDialog("Do you want to Cancel this order?", 2)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDataListItems() {

        orderNumberTextView.text = "Order No: ${orderModel?.orderId}"

        var sum = 0

        orderModel?.orderProductModelList?.forEach { x ->
            sum = sum.plus(x.quantity!!)
        }

        totalQuantityTextView.text = "Total Quantity: $sum"

    }

    private fun initProductItemsRecyclerView() {

        manageProductsRecyclerView.layoutManager = LinearLayoutManager(this)
        manageProductsRecyclerView.adapter =
            OrderProductRecyclerViewAdapter(this, true, orderModel?.orderProductModelList!!,
                object : OrderProductRecyclerViewAdapter.OrderProductRecyclerViewAdapterListener {

                    override fun onProductModified(
                        productsModel: OrderProductsModel,
                        list: ArrayList<OrderProductsModel>
                    ) {
                        var sum = 0
                        list.forEach {
                            sum = it.quantity!!.plus(sum)
                        }
                        totalQuantityTextView.text = "Total Qty: $sum"
                        if (sum != 0) {
                            enableCartLayout(true)
                        } else {
                            enableCartLayout(false)
                        }

                        updatedList = list

                    }
                })
        manageProductsRecyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun enableCartLayout(b: Boolean) {
        manageProductsRecyclerView.visibility = if (b) View.VISIBLE else View.GONE
    }


    private fun showConfirmationDialog(msg: String, type: Int) {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        //Now we need an AlertDialog.Builder object
        val builder = AlertDialog.Builder(this)
        //then we will inflate the custom alert dialog xml that we created
        val dialogView = LayoutInflater.from(this).inflate(R.layout.confirmation_dialog, viewGroup, false)


        dialogView.msgTextView.text = msg


        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView)

        //finally creating the alert dialog and displaying it
        val alertDialog = builder.create()

        dialogView.noButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.yesButton.setOnClickListener {
            alertDialog.dismiss()
            if (type == 1) {
                acceptOrder()
            } else {
                cancelOrder()
            }
        }


        alertDialog.show()
    }

    private fun cancelOrder() {
        if (::updatedList.isInitialized)
            orderModel?.orderProductModelList = updatedList

        loadingProgressBar.showProgress(this, "")

        val reference = FirebaseDatabase.getInstance().getReference("orders_table")
        val query = reference.orderByChild("orderId").equalTo(orderModel?.orderId)

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                loadingProgressBar.hideProgress()
            }

            override fun onDataChange(p0: DataSnapshot) {

                loadingProgressBar.hideProgress()
                if (p0.hasChildren()) {
                    var orderUpdated = false
                    for (d in p0.children) {
                        val model = d.getValue(OrderModel::class.java)
                        if (model?.orderId == orderModel?.orderId) {
                            val productList =  orderModel?.orderProductModelList
                            model?.orderProductModelList = productList!!
                            model?.orderStatus = OrderStatus.CANCEL_ORDER
                            d.ref.setValue(model)
                            orderUpdated = true
                            break
                        }
                    }

                    if (orderUpdated) {
                        Toast.makeText(
                            this@NewOrderDetailActivity, "Order Cancelled",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@NewOrderDetailActivity, "Order Not Cancelled.. Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish()
                } else {
                    Toast.makeText(
                        this@NewOrderDetailActivity, "Order Not Found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        query.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun acceptOrder() {

        if (::updatedList.isInitialized)
            orderModel?.orderProductModelList = updatedList

        loadingProgressBar.showProgress(this, "")

        val reference = FirebaseDatabase.getInstance().getReference("orders_table")
        val query = reference.orderByChild("orderId").equalTo(orderModel?.orderId)

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                loadingProgressBar.hideProgress()
            }

            override fun onDataChange(p0: DataSnapshot) {

                loadingProgressBar.hideProgress()
                if (p0.hasChildren()) {
                    var orderUpdated = false
                    for (d in p0.children) {
                        val model = d.getValue(OrderModel::class.java)
                        if (model!!.orderId == orderModel?.orderId) {
                            model.orderProductModelList = orderModel?.orderProductModelList!!
                            model.orderStatus = OrderStatus.IN_PROCESS
                            d.ref.setValue(model)
                            orderUpdated = true
                            break
                        }
                    }

                    if (orderUpdated) {
                        Toast.makeText(
                            this@NewOrderDetailActivity, "Order Accepted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@NewOrderDetailActivity, "Order Not Updated.. Please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()

                } else {
                    Toast.makeText(
                        this@NewOrderDetailActivity, "Order Not Found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        query.addListenerForSingleValueEvent(valueEventListener)
    }
}
