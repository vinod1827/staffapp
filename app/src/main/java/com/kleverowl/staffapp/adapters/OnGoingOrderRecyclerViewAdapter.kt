package com.kleverowl.staffapp.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.utils.OrderStatus
import com.kleverowl.staffapp.utils.StaffType
import com.kleverowl.staffapp.utils.Utils
import kotlinx.android.synthetic.main.single_ongoing_order.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OnGoingOrderRecyclerViewAdapter(
    private val context: Context, private val list: ArrayList<OrderModel>,
    private val upcomingOrderClickListener: OnGoingOrderClickListener
) :
    RecyclerView.Adapter<OnGoingOrderRecyclerViewAdapter.PastOrderViewHolder>() {

    private var type: Int = Utils.getStaffType(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastOrderViewHolder {
        return PastOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_ongoing_order,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: PastOrderViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.orderNoTextView.text =
            context.getString(R.string.order_no_text, item.orderId)


        if (type == StaffType.DELIVERY) {
            holder.itemView.dateTextView.text =
                item.billedDate?.let {
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it))
                }
            holder.itemView.timeTextView.text =
                item.billedDate?.let {
                    SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date(it))
                }
        } else {
            holder.itemView.dateTextView.text =
                item.processedOrderDate?.let {
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it))
                }
            holder.itemView.timeTextView.text =
                item.processedOrderDate?.let {
                    SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date(it))
                }
        }

        var sum = 0

        item.orderProductModelList?.let {
            for (d in it) {
                sum = sum.plus(d.quantity ?: 0)
            }
        }

        holder.itemView.totalQuantityTextView.text =
            context.getString(R.string.total_quantity_text, sum)

        var msg = context.getString(R.string.pending_status)
        var color = 0
        when (item.orderStatus) {
            OrderStatus.PENDING -> {
                msg = context.getString(R.string.pending_status)
                color = Color.parseColor("#817516")
            }
            OrderStatus.ACCEPTED -> {
                msg = context.getString(R.string.accepted_status)
                color = Color.parseColor("#009688")
            }

            OrderStatus.DELIVERED -> {
                msg = context.getString(R.string.billed_delivered_status)
                color = Color.parseColor("#009688")
            }
            OrderStatus.DISPATCHED -> {
                msg = context.getString(R.string.dispatched_status)
                color = Color.parseColor("#817516")
            }
            OrderStatus.IN_PROCESS -> {
                msg = context.getString(R.string.in_process_status)
                color = Color.parseColor("#817516")
            }
        }
        holder.itemView.orderStatusTextView.text = msg
        holder.itemView.orderStatusTextView.setTextColor(color)


        //TimeFormatUtil.getRelativeTimeSpanStringShort(context, item.date)

        holder.itemView.companyNameTextView.text = item.companyName
        holder.itemView.productItemsReyclerView.adapter =
            item.orderProductModelList?.let { ProductOrderRecyclerViewAdapter(context, it) }
        holder.itemView.productItemsReyclerView.adapter?.notifyDataSetChanged()
        holder.itemView.readyToDispatchTextView.setOnClickListener {

            if (Utils.getStaffType(context) == StaffType.STAFF) {
                upcomingOrderClickListener.readyToDispatchClicked(item)
            } else {
                upcomingOrderClickListener.delivered(item)
            }

        }

        if (Utils.getStaffType(context) == StaffType.STAFF) {
            holder.itemView.readyToDispatchTextView.text = context.getString(R.string.ready_to_dispatch)
        } else {
            holder.itemView.readyToDispatchTextView.text = context.getString(R.string.delivered)
        }
    }


    interface OnGoingOrderClickListener {
        fun readyToDispatchClicked(item: OrderModel)
        fun delivered(item: OrderModel)
    }

    class PastOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.productItemsReyclerView.layoutManager = LinearLayoutManager(itemView.context)
        }
    }
}