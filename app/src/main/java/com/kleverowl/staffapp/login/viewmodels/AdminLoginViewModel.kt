package com.kleverowl.staffapp.login.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.kleverowl.staffapp.login.repositories.AdminLoginRespository
import com.kleverowl.staffapp.models.DeliveryModel
import com.kleverowl.staffapp.models.StaffModel
import com.kleverowl.staffapp.utils.PreferenceConnector
import com.kleverowl.staffapp.utils.StaffType

class AdminLoginViewModel(application: Application) : AndroidViewModel(application) {

    fun login(type: Int, phoneNumber: String, key: String): LiveData<Boolean> =
        if (type == StaffType.DELIVERY)
            AdminLoginRespository.deliveryBoyLogin(getApplication(), phoneNumber, key) else
            AdminLoginRespository.staffLogin(getApplication(), phoneNumber, key)
}