package com.vinu.vinodassigment.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.kleverowl.staffapp.models.OrderProductsModel

class Converters {
    @TypeConverter
    fun fromArrayLisr(value: List<OrderProductsModel>?) = Gson().toJson(value)

    @TypeConverter
    fun fromString(value: String) = Gson().fromJson(value, Array<OrderProductsModel>::class.java).toList()
}