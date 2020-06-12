package com.kleverowl.staffapp.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kleverowl.staffapp.models.OrderProductsModel
import com.kleverowl.staffapp.R
import kotlinx.android.synthetic.main.single_order_product_item.view.*

class ProductOrderRecyclerViewAdapter(
    private val context: Context, private val list: List<OrderProductsModel>
) :
    RecyclerView.Adapter<ProductOrderRecyclerViewAdapter.ProductRecyclerViewAdapterHolder>() {


    private var productRecyclerViewAdapterListener: ProductOrderRecyclerViewAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRecyclerViewAdapterHolder {
        return ProductRecyclerViewAdapterHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_order_product_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: ProductRecyclerViewAdapterHolder, position: Int) {
        val item = list.get(position)
        holder.itemView.productNameTextView.text = item.name
        holder.itemView.quantityTextView.text = "Qty: ${item.quantity!!}"
        holder.itemView.singleProductOrderItemLayout.setOnClickListener {
            if (this.productRecyclerViewAdapterListener != null) {
                productRecyclerViewAdapterListener!!.onProductOrderClicked(item)
            }
        }
    }

    fun setProductRecyclerViewAdapterListener(productOrderRecyclerViewAdapterListener: ProductOrderRecyclerViewAdapterListener) {
        this.productRecyclerViewAdapterListener = productOrderRecyclerViewAdapterListener
    }

    interface ProductOrderRecyclerViewAdapterListener {
        fun onProductOrderClicked(orderProductsModel: OrderProductsModel)
    }

    class ProductRecyclerViewAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}