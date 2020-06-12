package com.kleverowl.staffapp.orders.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.adapters.PastOrderRecyclerViewAdapter
import com.kleverowl.staffapp.components.LoadingProgressBar
import com.kleverowl.staffapp.fragments.BaseFragment
import com.kleverowl.staffapp.orders.presenter.OrderInteractorImplementation
import com.kleverowl.staffapp.orders.presenter.OrderPresenterImplementation
import kotlinx.android.synthetic.main.fragment_past_orders.*


class PastOrderFragment : BaseFragment(), ListOrdersView {

    private lateinit var orderPresenter: OrderPresenterImplementation

    private val mainList: ArrayList<OrderModel> = ArrayList()
    private lateinit var loadingProgressBar: LoadingProgressBar

    private var listener: PastOrderFragmentAttachedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingProgressBar = LoadingProgressBar()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_past_orders, container, false)
    }


    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            fetchDetails()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeNewOrdersRecylerView()

        orderPresenter = OrderPresenterImplementation(this, OrderInteractorImplementation())
        activity?.let { orderPresenter.setDetails(it) }

        swipeRefreshLayout.setOnRefreshListener {
            if (swipeRefreshLayout.isRefreshing) {
                fetchDetails()
            }
        }
    }

    private fun enableListView(b: Boolean) {
        pastOrdersListView.visibility = if (b) View.VISIBLE else View.GONE
        noPastOrdersTextView.visibility = if (!b) View.VISIBLE else View.GONE
    }

    private fun fetchDetails() {
        swipeRefreshLayout?.isRefreshing = true
        orderPresenter.getPastOrders()
    }

    private fun initializeNewOrdersRecylerView() {
        pastOrdersListView.layoutManager = LinearLayoutManager(activity)
        pastOrdersListView.adapter =
            activity?.let {
                PastOrderRecyclerViewAdapter(
                    it,
                    mainList
                )
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PastOrderFragmentAttachedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement PastOrderFragmentAttachedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface PastOrderFragmentAttachedListener {
        fun onOrderClicked(orderModel: OrderModel)
    }

    override fun showProgress() {
        swipeRefreshLayout?.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun onOrderLoaded(list: ArrayList<OrderModel>) {
        if (list.isNotEmpty()) {
            mainList.clear()
            mainList.addAll(list)
            pastOrdersListView.adapter?.notifyDataSetChanged()
            enableListView(true)
        } else {
            enableListView(false)
        }
    }

    override fun onError(message: String) {
        if (message == "No Orders Yet") {
            enableListView(false)
        }
        swipeRefreshLayout.isRefreshing = false
        loadingProgressBar.hideProgress()
    }

    override fun orderProcessed(message: String) {
    }

    override fun orderDispatched(message: String) {
    }

    override fun orderPickedup(message: String) {
    }

    override fun onDelivered(message: String) {

    }
}
