package com.kleverowl.staffapp.adapters

import android.content.Context
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
import kotlinx.android.synthetic.main.single_past_order.view.*
import kotlinx.android.synthetic.main.single_past_order.view.dateTextView
import kotlinx.android.synthetic.main.single_past_order.view.orderNoTextView
import kotlinx.android.synthetic.main.single_past_order.view.productItemsReyclerView
import kotlinx.android.synthetic.main.single_past_order.view.totalQuantityTextView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PastOrderRecyclerViewAdapter(
    private val context: Context,
    private val list: ArrayList<OrderModel>
) :
    RecyclerView.Adapter<PastOrderRecyclerViewAdapter.PastOrderViewHolder>() {

    private var type: Int = Utils.getStaffType(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastOrderViewHolder {
        return PastOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_past_order,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: PastOrderViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.orderNoTextView.text =
            context.getString(R.string.order_no_text, item.orderId)


        if (type == StaffType.DELIVERY) {
            holder.itemView.dateTextView.text =
                item.deliveryDateTime?.let {
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it))
                }
            holder.itemView.timeTextView.text =
                item.deliveryDateTime?.let {
                    SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date(it))
                }
        } else {
            holder.itemView.dateTextView.text =
                item.readyToDispatchDate?.let {
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it))
                }
            holder.itemView.timeTextView.text =
                item.readyToDispatchDate?.let {
                    SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date(it))
                }
        }


        var sum = 0
        item.orderProductModelList?.let {
            for (d in it) {
                sum = sum.plus(d.quantity ?: 0)
            }
        }

        holder.itemView.companyNameTextView.text = item.companyName

        holder.itemView.totalQuantityTextView.text =
            context.getString(R.string.total_quantity_text, sum)

        holder.itemView.productItemsReyclerView.adapter =
            item.orderProductModelList?.let {
                ProductOrderRecyclerViewAdapter(context, it)
            }

        holder.itemView.productItemsReyclerView.adapter?.notifyDataSetChanged()

        var msg = ""
        when (item.orderStatus) {
            OrderStatus.PENDING -> msg = context.getString(R.string.pending_status)
            OrderStatus.ACCEPTED -> msg = context.getString(R.string.accepted_status)
            OrderStatus.DELIVERED -> msg = context.getString(R.string.billed_delivered_status)
            OrderStatus.DISPATCHED -> msg = context.getString(R.string.dispatched_status)
            OrderStatus.IN_PROCESS -> msg = context.getString(R.string.in_process_status)
        }

        holder.itemView.orderStatusTextView.text = msg
    }


    class PastOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.productItemsReyclerView.layoutManager = LinearLayoutManager(itemView.context)
        }
    }
}