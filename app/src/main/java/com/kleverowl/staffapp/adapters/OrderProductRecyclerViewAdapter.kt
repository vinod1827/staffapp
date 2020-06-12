package com.kleverowl.staffapp.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.models.OrderProductsModel
import kotlinx.android.synthetic.main.order_summary_product.view.*

class OrderProductRecyclerViewAdapter(
    private val context: Context, private val isCart: Boolean, private val list: List<OrderProductsModel>,
    private val productRecyclerViewAdapterListener: OrderProductRecyclerViewAdapterListener
) :
    RecyclerView.Adapter<OrderProductRecyclerViewAdapter.OrderProductRecyclerViewAdapterHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductRecyclerViewAdapterHolder {
        return OrderProductRecyclerViewAdapterHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.order_summary_product,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: OrderProductRecyclerViewAdapterHolder, position: Int) {
        val item = list.get(position)
        holder.itemView.productNameTextView.text = item.name
        holder.itemView.productCodeTextView.text = item.productCode

        if (isCart) {
            holder.itemView.quantityView.value = item.quantity!!
            enableQuantityControl(holder, true)
        } else enableQuantityControl(holder, false)

        if (item.quantity!! > 0) {
            enableQuantityControl(holder, true)
            holder.itemView.quantityView.value = item.quantity!!
        }

        holder.itemView.quantityView.setValueChangedListener { value, _ ->
            item.quantity = value
            if (value == 0) {
                enableQuantityControl(holder, false)
            }

            productRecyclerViewAdapterListener.onProductModified(item, list as ArrayList<OrderProductsModel>)
        }

        holder.itemView.addButton.setOnClickListener {
            enableQuantityControl(holder, true)
            holder.itemView.quantityView.value = 1
            item.quantity = 1

            productRecyclerViewAdapterListener.onProductModified(item, list as ArrayList<OrderProductsModel>)
        }

    }

    private fun enableQuantityControl(holder: OrderProductRecyclerViewAdapterHolder, b: Boolean) {
        holder.itemView.addButton.visibility = if (!b) View.VISIBLE else View.GONE
        holder.itemView.quantityView.visibility = if (b) View.VISIBLE else View.GONE
    }


    class OrderProductRecyclerViewAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OrderProductRecyclerViewAdapterListener {
        fun onProductModified(productsModel: OrderProductsModel, list: ArrayList<OrderProductsModel>)
    }
}