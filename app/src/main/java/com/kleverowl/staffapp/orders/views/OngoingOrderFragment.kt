package com.kleverowl.staffapp.orders.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.adapters.OnGoingOrderRecyclerViewAdapter
import com.kleverowl.staffapp.components.LoadingProgressBar
import com.kleverowl.staffapp.fragments.BaseFragment
import com.kleverowl.staffapp.orders.presenter.OrderInteractorImplementation
import com.kleverowl.staffapp.orders.presenter.OrderPresenter
import com.kleverowl.staffapp.orders.presenter.OrderPresenterImplementation
import kotlinx.android.synthetic.main.fragment_past_orders.*


class OngoingOrderFragment : BaseFragment(), ListOrdersView {

    private lateinit var orderPresenter: OrderPresenter

    private lateinit var loadingProgressBar: LoadingProgressBar
    private val mainList: ArrayList<OrderModel> = ArrayList()

    private var listener: OnGoingFragmentAttachedListener? = null


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeOnGoingOrdersRecylerView()
        orderPresenter = OrderPresenterImplementation(this, OrderInteractorImplementation())
        orderPresenter.setDetails(activity!!)
        swipeRefreshLayout.setOnRefreshListener {
            if (swipeRefreshLayout.isRefreshing) {
                fetchDetails()
            }
        }
    }


    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            fetchDetails()
        }
    }

    private fun initializeOnGoingOrdersRecylerView() {
        pastOrdersListView.layoutManager = LinearLayoutManager(activity)
        pastOrdersListView.adapter =
            activity?.let {
                OnGoingOrderRecyclerViewAdapter(
                    it,
                    mainList,
                    object : OnGoingOrderRecyclerViewAdapter.OnGoingOrderClickListener {
                        override fun delivered(item: OrderModel) {
                            deliverOrder(item)
                        }

                        private fun deliverOrder(item: OrderModel) {
                            loadingProgressBar.showProgress(activity, "")
                            orderPresenter.delivered(item)
                        }

                        override fun readyToDispatchClicked(item: OrderModel) {
                            readyOrderToDispatch(item)
                        }

                        private fun readyOrderToDispatch(item: OrderModel) {
                            loadingProgressBar.showProgress(activity, "")
                            orderPresenter.readyToDispatch(item)
                        }
                    })
            }
    }

    private fun enableListView(b: Boolean) {
        pastOrdersListView.visibility = if (b) View.VISIBLE else View.GONE
        noPastOrdersTextView.visibility = if (!b) View.VISIBLE else View.GONE
    }

    private fun fetchDetails() {
        swipeRefreshLayout.isRefreshing = true
        orderPresenter.getOnGoingOrders()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGoingFragmentAttachedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnGoingFragmentAttachedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnGoingFragmentAttachedListener {
        fun onGoingOrderClicked(orderModel: OrderModel)
    }

    override fun showProgress() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onOrderLoaded(list: ArrayList<OrderModel>) {
        loadingProgressBar.hideProgress()
        swipeRefreshLayout.isRefreshing = false
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
        loadingProgressBar.hideProgress()
        fetchDetails()
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun orderPickedup(message: String) {
    }

    override fun onDelivered(message: String) {
        loadingProgressBar.hideProgress()
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        fetchDetails()
    }
}
