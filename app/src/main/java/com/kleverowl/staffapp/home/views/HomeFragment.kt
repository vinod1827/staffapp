/*
package com.kleverowl.plywoodadmin.home.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.kleverowl.plywoodadmin.R
import com.kleverowl.plywoodadmin.adapters.CatalogRecyclerViewAdapter
import com.kleverowl.plywoodadmin.adapters.CataloguePagerAdapter
import com.kleverowl.plywoodadmin.components.LoadingProgressBar
import com.kleverowl.plywoodadmin.components.LoopingViewPager
import com.kleverowl.plywoodadmin.fragments.BaseFragment
import com.kleverowl.plywoodadmin.managecatalogue.viewmodels.CatalogueViewModel
import com.kleverowl.plywoodadmin.models.CatalogueModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.catalogueRecyclerView


class HomeFragment : BaseFragment() {

    private lateinit var catalogueViewModel: CatalogueViewModel
    private lateinit var loadingProgressBar: LoadingProgressBar
    private var list = ArrayList<CatalogueModel>()

    private var listener: HomeFragmentAttachedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catalogueViewModel = ViewModelProviders.of(this).get(CatalogueViewModel::class.java)
        loadingProgressBar = LoadingProgressBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catalogueRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        catalogueRecyclerView.adapter =
            CatalogRecyclerViewAdapter(
                activity!!,
                list,
                object : CatalogRecyclerViewAdapter.CatalogRecyclerViewListener {
                    override fun onCatalogueSelected(catalogueModel: CatalogueModel) {
                    }

                })

        fetchCatalogues()
    }

    private fun fetchCatalogues() {
        loadingProgressBar.showProgress(activity!!, "")
        catalogueViewModel.getCatalogue().observe(this, Observer {
            loadingProgressBar.hideProgress()
            if (it != null && it.size > 0) {
                list.clear()
                list.addAll(it)
                catalogueRecyclerView.adapter!!.notifyDataSetChanged()
                enableRecyclerView(true)

                displayPager(it)
            } else {
                enableRecyclerView(false)
            }
        })
    }

    private fun displayPager(it: ArrayList<CatalogueModel>?) {
        val adapter = CataloguePagerAdapter(activity!!, it, true)
        viewPager.adapter = adapter
        adapter.notifyDataSetChanged()
        indicator.count = viewPager.indicatorCount


        viewPager.setIndicatorPageChangeListener(object : LoopingViewPager.IndicatorPageChangeListener {
            override fun onIndicatorProgress(selectingPosition: Int, progress: Float) {
                indicator.setProgress(selectingPosition, progress)
            }

            override fun onIndicatorPageChange(newIndicatorPosition: Int) {
                //                indicatorView.setSelection(newIndicatorPosition);
            }
        })
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            viewPager.resumeAutoScroll()
        }
    }

    override fun onPause() {
        super.onPause()
        viewPager.pauseAutoScroll()
    }

    private fun enableRecyclerView(b: Boolean) {
        catalogueRecyclerView.visibility = if (b) View.VISIBLE else View.GONE
        catalogueCaptionLayout.visibility = if (b) View.GONE else View.VISIBLE
        mainLayout.visibility = if (b) View.VISIBLE else View.GONE
        listEmptyTextView.visibility = if (b) View.GONE else View.VISIBLE
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeFragmentAttachedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement AccountsFragmentAttachedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface HomeFragmentAttachedListener {
        fun onHomeFragmentAttached()
    }
}
*/
