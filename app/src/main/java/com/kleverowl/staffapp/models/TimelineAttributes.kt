package com.kleverowl.plywoodadmin.models

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlin.properties.Delegates.observable

@Parcelize
class TimelineAttributes(
        var markerSize: Int,
        var markerColor: Int,
        var markerInCenter: Boolean,
        var linePadding: Int,
        var lineWidth: Int,
        var startLineColor: Int,
        var endLineColor: Int,
        var lineStyle: Int,
        var lineDashWidth: Int,
        var lineDashGap: Int
): Parcelable {

    @IgnoredOnParcel
    var orientation by observable(Orientation.VERTICAL) { _, oldValue, newValue ->
        onOrientationChanged?.invoke(oldValue, newValue)
    }

    @IgnoredOnParcel
    var onOrientationChanged: ((Orientation, Orientation) -> Unit)? = null

    override fun toString(): String {
        return "TimelineAttributes(markerSize=$markerSize, markerColor=$markerColor, markerInCenter=$markerInCenter, linePadding=$linePadding, lineWidth=$lineWidth, startLineColor=$startLineColor, endLineColor=$endLineColor, lineStyle=$lineStyle, lineDashWidth=$lineDashWidth, lineDashGap=$lineDashGap, onOrientationChanged=$onOrientationChanged)"
    }

    fun copy(): TimelineAttributes {
        val attributes = TimelineAttributes(markerSize, markerColor, markerInCenter, linePadding, lineWidth, startLineColor, endLineColor, lineStyle, lineDashWidth, lineDashGap)
        attributes.orientation = orientation
        return attributes
    }
}