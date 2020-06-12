package com.kleverowl.staffapp.orders.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.fragments.BaseFragment
import com.kleverowl.staffapp.components.LoadingProgressBar
import kotlinx.android.synthetic.main.fragment_orders_list.*


class OrderFragment : BaseFragment() {

    private lateinit var loadingProgressBar: LoadingProgressBar
    private var list = ArrayList<OrderModel>()

    private var listener: OrderFragmentAttachedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingProgressBar = LoadingProgressBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = SimpleFragmentPagerAdapter(activity!!, childFragmentManager)
        viewPager.offscreenPageLimit = 1
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {

            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OrderFragmentAttachedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OrderFragmentAttachedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OrderFragmentAttachedListener {
        fun onOrderClicked(orderModel: OrderModel)
    }

    inner class SimpleFragmentPagerAdapter(private val mContext: Context, fm: FragmentManager) :
        FragmentPagerAdapter(fm) {

        // This determines the fragment for each tab
        override fun getItem(position: Int): Fragment {

            if (position == 0) {
                return NewOrderFragment()
            } else if (position == 1) {
                return OngoingOrderFragment()
            } else if (position == 2) {
                return PastOrderFragment()
            } else {
                return NewOrderFragment()
            }

        }

        // This determines the number of tabs
        override fun getCount(): Int {
            return 3
        }

        // This determines the title for each tab
        override fun getPageTitle(position: Int): CharSequence? {
            // Generate title based on item position
            when (position) {
                0 -> return mContext.getString(R.string.new_orders)
                1 -> return mContext.getString(R.string.ongoing_orders)
                2 -> return mContext.getString(R.string.past_orders)
                else -> return null
            }
        }

    }

}

