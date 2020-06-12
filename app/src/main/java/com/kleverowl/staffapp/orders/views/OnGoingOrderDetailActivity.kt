package com.kleverowl.staffapp.orders.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.utils.AppConstants
import kotlinx.android.synthetic.main.toolbar.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kleverowl.customerapp.models.OrderStatus
import com.kleverowl.plywoodadmin.models.Orientation
import com.kleverowl.plywoodadmin.models.TimelineAttributes
import com.kleverowl.staffapp.models.TimeLineModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.adapters.ProductOrderRecyclerViewAdapter
import com.kleverowl.staffapp.adapters.TimeLineAdapter
import com.kleverowl.staffapp.components.LoadingProgressBar
import com.kleverowl.staffapp.components.TimelineView
import com.kleverowl.staffapp.utils.TimeFormatUtil
import com.kleverowl.staffapp.utils.Utils
import kotlinx.android.synthetic.main.on_going_order_detail_activity.*


class OnGoingOrderDetailActivity : AppCompatActivity() {

    private lateinit var loadingProgressBar: LoadingProgressBar
    private var orderModel: OrderModel? = null
    private lateinit var mAdapter: TimeLineAdapter
    private val mDataList = ArrayList<TimeLineModel>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAttributes: TimelineAttributes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.on_going_order_detail_activity)
        loadingProgressBar = LoadingProgressBar()
        setSupportActionBar(toolbar)


        orderModel = intent.extras?.getParcelable(AppConstants.ORDER_MODEL)

        supportActionBar!!.title = "New Order"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_product)
        toolbarTitle.text = ""
        toolbarTitle.visibility = View.GONE


        //default values
        mAttributes = TimelineAttributes(
            markerSize = Utils.dpToPx(20f, this),
            markerColor = ContextCompat.getColor(this, R.color.disabled_text_color),
            markerInCenter = true,
            linePadding = Utils.dpToPx(2f, this),
            startLineColor = ContextCompat.getColor(this, R.color.disabled_text_color),
            endLineColor = ContextCompat.getColor(this, R.color.disabled_text_color),
            lineStyle = TimelineView.LineStyle.NORMAL,
            lineWidth = Utils.dpToPx(2f, this),
            lineDashWidth = Utils.dpToPx(4f, this),
            lineDashGap = Utils.dpToPx(5f, this)
        )
        mAttributes.orientation = Orientation.HORIZONTAL

        setDataListItems()
        initProductItemsRecyclerView()
        initRecyclerView()
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
                if (orderModel?.orderStatus == com.kleverowl.staffapp.utils.OrderStatus.PENDING) OrderStatus.ACTIVE
                else OrderStatus.INACTIVE
            )
        )
        mDataList.add(
            TimeLineModel(
                "Accepted",
                if (orderModel?.orderStatus == com.kleverowl.staffapp.utils.OrderStatus.ACCEPTED) OrderStatus.ACTIVE
                else OrderStatus.INACTIVE
            )
        )
        mDataList.add(
            TimeLineModel(
                "In Process",
                if (orderModel?.orderStatus == com.kleverowl.staffapp.utils.OrderStatus.IN_PROCESS) OrderStatus.ACTIVE
                else OrderStatus.INACTIVE
            )
        )
        mDataList.add(
            TimeLineModel(
                "Dispatched",
                if (orderModel?.orderStatus == com.kleverowl.staffapp.utils.OrderStatus.DISPATCHED) OrderStatus.ACTIVE
                else OrderStatus.INACTIVE
            )
        )
        mDataList.add(
            TimeLineModel(
                "Delivered",
                if (orderModel?.orderStatus == com.kleverowl.staffapp.utils.OrderStatus.DELIVERED) OrderStatus.ACTIVE
                else OrderStatus.INACTIVE
            )
        )
    }

    private fun initProductItemsRecyclerView() {
        productItemsReyclerView.layoutManager = LinearLayoutManager(this)
        productItemsReyclerView.adapter =
            ProductOrderRecyclerViewAdapter(this, orderModel?.orderProductModelList!!)
        productItemsReyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        initAdapter()

        orderStatusRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("LongLogTag")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //dropshadow.visibility = if(recyclerView.getChildAt(0).top < 0) View.VISIBLE else View.GONE
            }
        })
    }

    private fun initAdapter() {

        mLayoutManager = if (mAttributes.orientation == Orientation.HORIZONTAL) {
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        } else {
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        }

        orderStatusRecyclerView.layoutManager = mLayoutManager

        mAdapter = TimeLineAdapter(mDataList, mAttributes)
        orderStatusRecyclerView.adapter = mAdapter
    }
}
