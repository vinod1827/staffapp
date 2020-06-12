package com.kleverowl.staffapp.adapters

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kleverowl.customerapp.models.OrderStatus
import com.kleverowl.plywoodadmin.models.Orientation
import com.kleverowl.plywoodadmin.models.TimelineAttributes
import com.kleverowl.plywoodadmin.utils.VectorDrawableUtils
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.components.TimelineView
import com.kleverowl.staffapp.models.TimeLineModel
import kotlinx.android.synthetic.main.item_timeline.view.*

/**
 * Created by Vipul Asri on 05-12-2015.
 */

class TimeLineAdapter(private val mFeedList: List<TimeLineModel>, private var mAttributes: TimelineAttributes) :
    RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        view = if (mAttributes.orientation == Orientation.HORIZONTAL) {
            layoutInflater.inflate(R.layout.item_timeline_horizontal, parent, false)
        } else {
            layoutInflater.inflate(R.layout.item_timeline, parent, false)
        }
        return TimeLineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]

        holder.message.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.disabled_text_color
            )
        )

        when {
            timeLineModel.status == OrderStatus.INACTIVE -> {
                holder.timeline.marker = VectorDrawableUtils.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_marker_inactive,
                    mAttributes.markerColor
                )
            }
            timeLineModel.status == OrderStatus.ACTIVE -> {
                holder.message.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.login_button_color
                    )
                )
                holder.timeline.marker = VectorDrawableUtils.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_marker_active,
                    mAttributes.markerColor
                )
            }
            else -> {
                holder.timeline.setMarker(
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_marker),
                    mAttributes.markerColor
                )
            }
        }

        holder.message.text = timeLineModel.message
    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        val message = itemView.text_timeline_title
        val timeline = itemView.timeline

        init {
            timeline.initLine(viewType)
            timeline.markerSize = mAttributes.markerSize
            timeline.setMarkerColor(mAttributes.markerColor)
            timeline.isMarkerInCenter = mAttributes.markerInCenter
            timeline.linePadding = mAttributes.linePadding

            timeline.lineWidth = mAttributes.lineWidth
            timeline.setStartLineColor(mAttributes.startLineColor, viewType)
            timeline.setEndLineColor(mAttributes.endLineColor, viewType)
            timeline.lineStyle = mAttributes.lineStyle
            timeline.lineStyleDashLength = mAttributes.lineDashWidth
            timeline.lineStyleDashGap = mAttributes.lineDashGap
        }
    }

}
