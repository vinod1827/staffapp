package com.kleverowl.staffapp.orders.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.utils.AppConstants
import kotlinx.android.synthetic.main.toolbar.*
import com.kleverowl.staffapp.models.TimeLineModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.adapters.ProductOrderRecyclerViewAdapter
import com.kleverowl.staffapp.components.LoadingProgressBar
import com.kleverowl.staffapp.utils.OrderStatus
import com.kleverowl.staffapp.utils.TimeFormatUtil
import kotlinx.android.synthetic.main.activity_past_order_detail.*


class PastOrderDetailActivity : AppCompatActivity() {

    private lateinit var loadingProgressBar: LoadingProgressBar
    private var orderModel: OrderModel?=null
    private val mDataList = ArrayList<TimeLineModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_past_order_detail)
        loadingProgressBar = LoadingProgressBar()
        setSupportActionBar(toolbar)


        orderModel = intent.extras?.getParcelable(AppConstants.ORDER_MODEL)

        supportActionBar!!.title = "New Order"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_product)
        toolbarTitle.text = ""
        toolbarTitle.visibility = View.GONE

        setDataListItems()
        initProductItemsRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDataListItems() {

        orderCustomerTextView.text = orderModel?.customerName
        orderNumberTextView.text = "Order No: ${orderModel?.orderId}"
        val date = TimeFormatUtil.getRelativeTimeSpanStringShort(
            this,
            orderModel?.date?:0L
        )
        orderDateTextView.text = date
        customerAddressTextView.text = "Delivery Address: ${orderModel?.address}"
        contactPersonTextView.text = "Contact Person: ${orderModel?.contactPersonInfo}"

        var sum = 0

        orderModel?.orderProductModelList!!.forEach { x ->
            sum = sum.plus(x.quantity!!)
        }

        totalQuantityTextView.text = "Total Quantity: $sum"

        mDataList.add(
            TimeLineModel(
                "Pending",
                if (orderModel?.orderStatus == OrderStatus.PENDING) com.kleverowl.customerapp.models.OrderStatus.ACTIVE
                else com.kleverowl.customerapp.models.OrderStatus.INACTIVE
            )
        )
        mDataList.add(
            TimeLineModel(
                "Accepted",
                if (orderModel?.orderStatus == OrderStatus.ACCEPTED) com.kleverowl.customerapp.models.OrderStatus.ACTIVE
                else com.kleverowl.customerapp.models.OrderStatus.INACTIVE
            )
        )
        mDataList.add(
            TimeLineModel(
                "In Process",
                if (orderModel?.orderStatus == OrderStatus.IN_PROCESS) com.kleverowl.customerapp.models.OrderStatus.ACTIVE
                else com.kleverowl.customerapp.models.OrderStatus.INACTIVE
            )
        )
        mDataList.add(
            TimeLineModel(
                "Dispatched",
                if (orderModel?.orderStatus == OrderStatus.DISPATCHED) com.kleverowl.customerapp.models.OrderStatus.ACTIVE
                else com.kleverowl.customerapp.models.OrderStatus.INACTIVE
            )
        )
        mDataList.add(
            TimeLineModel(
                "Delivered",
                if (orderModel?.orderStatus == OrderStatus.DELIVERED) com.kleverowl.customerapp.models.OrderStatus.ACTIVE
                else com.kleverowl.customerapp.models.OrderStatus.INACTIVE
            )
        )
    }

    private fun initProductItemsRecyclerView() {
        productItemsReyclerView.layoutManager = LinearLayoutManager(this)
        productItemsReyclerView.adapter =
            ProductOrderRecyclerViewAdapter(this, orderModel?.orderProductModelList!!)
        productItemsReyclerView.adapter!!.notifyDataSetChanged()
    }

}
