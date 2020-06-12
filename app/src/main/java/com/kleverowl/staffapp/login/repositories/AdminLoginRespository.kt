package com.kleverowl.staffapp.login.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import com.google.gson.Gson
import com.kleverowl.staffapp.utils.StaffType
import com.kleverowl.staffapp.models.DeliveryModel
import com.kleverowl.staffapp.models.StaffModel
import com.kleverowl.staffapp.utils.PreferenceConnector
import kotlin.coroutines.CoroutineContext


object AdminLoginRespository {

    fun staffLogin(
        application: Application,
        number: String,
        key: String
    ): LiveData<Boolean> {
        return object : LiveData<Boolean>() {
            override fun onActive() {
                super.onActive()
                val reference = FirebaseDatabase.getInstance().getReference("staffModel")
                val query = reference.orderByChild("mobileNumber").equalTo(number)
                val valueEventListener = object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        value = false
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.hasChildren()) {
                            val model = p0.children.first().getValue(StaffModel::class.java)
                            if (model?.password == key) {
                                PreferenceConnector.writeString(
                                    application,
                                    PreferenceConnector.USER_DETAILS,
                                    Gson().toJson(model)
                                )
                                PreferenceConnector.writeInteger(
                                    application,
                                    PreferenceConnector.TYPE,
                                    StaffType.STAFF
                                )
                                value = true
                            } else {
                                value = false
                            }
                        } else {
                            value = false
                        }
                    }

                }
                query.addListenerForSingleValueEvent(valueEventListener)
            }
        }
    }


    fun deliveryBoyLogin(
        application: Application,
        number: String,
        key: String
    ): LiveData<Boolean> {
        return object : LiveData<Boolean>() {
            override fun onActive() {
                super.onActive()
                val reference = FirebaseDatabase.getInstance().getReference("delivery_boy")
                val query = reference.orderByChild("mobileNumber").equalTo(number)
                val valueEventListener = object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        value = false
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.hasChildren()) {
                            val model = p0.children.first().getValue(DeliveryModel::class.java)
                            if (model?.password == key) {
                                PreferenceConnector.writeString(
                                    application,
                                    PreferenceConnector.USER_DETAILS,
                                    Gson().toJson(model)
                                )
                                PreferenceConnector.writeInteger(
                                    application,
                                    PreferenceConnector.TYPE,
                                    StaffType.DELIVERY
                                )
                                value = true
                            } else {
                                value = false
                            }
                        } else {
                            value = false
                        }
                    }

                }
                query.addListenerForSingleValueEvent(valueEventListener)
            }
        }
    }
}