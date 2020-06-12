package com.kleverowl.staffapp.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.utils.StaffType
import com.kleverowl.staffapp.utils.Utils
import kotlinx.android.synthetic.main.single_new_order.view.*
import kotlinx.android.synthetic.main.single_past_order.view.dateTextView
import kotlinx.android.synthetic.main.single_past_order.view.orderNoTextView
import kotlinx.android.synthetic.main.single_past_order.view.productItemsReyclerView
import kotlinx.android.synthetic.main.single_past_order.view.totalQuantityTextView
import java.text.SimpleDateFormat
import java.util.*


class NewOrderRecyclerViewAdapter(
    private val context: Context, private val list: ArrayList<OrderModel>,
    private val newOrderClickListener: NewOrderClickListener
) :
    RecyclerView.Adapter<NewOrderRecyclerViewAdapter.NewOrderViewHolder>() {

    private var type: Int = Utils.getStaffType(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewOrderViewHolder {
        return NewOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_new_order,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: NewOrderViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.orderNoTextView.text =
            context.getString(R.string.order_no_text, item.orderId)
        var sum = 0
        item.orderProductModelList?.let {
            for (d in it) {
                sum = sum.plus(d.quantity ?: 0)
            }
        }


        if (type == StaffType.DELIVERY) {
            holder.itemView.dateTextView.text =
                item.readyToDispatchDate?.let {
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it))
                }
            holder.itemView.timeTextView.text =
                item.readyToDispatchDate?.let {
                    SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date(it))
                }
        } else {
            holder.itemView.dateTextView.text =
                item.acceptedOrderDate?.let {
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it))
                }
            holder.itemView.timeTextView.text =
                item.acceptedOrderDate?.let {
                    SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date(it))
                }
        }

        holder.itemView.totalQuantityTextView.text =
            context.getString(R.string.total_quantity_text, sum)



        holder.itemView.productItemsReyclerView.adapter =
            item.orderProductModelList?.let {
                ProductOrderRecyclerViewAdapter(context, it)
            }
        holder.itemView.productItemsReyclerView.adapter?.notifyDataSetChanged()
        holder.itemView.companyNameTextView.text = item.companyName

        if (Utils.getStaffType(context) == StaffType.STAFF) {
            holder.itemView.processOrderTextView.text = context.getString(R.string.process_order)
        } else if (Utils.getStaffType(context) == StaffType.DELIVERY) {
            holder.itemView.processOrderTextView.text = context.getString(R.string.billed)
        }

        holder.itemView.processOrderTextView.setOnClickListener {
            if (Utils.getStaffType(context) == StaffType.STAFF) {
                newOrderClickListener.processOrder(item)
            } else {
                newOrderClickListener.orderPickup(item)
            }

        }
    }


    interface NewOrderClickListener {
        fun processOrder(item: OrderModel)
        fun orderPickup(item: OrderModel)
        fun onCallClicked(item: OrderModel)
    }

    class NewOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.productItemsReyclerView.layoutManager = LinearLayoutManager(itemView.context)
        }
    }
}