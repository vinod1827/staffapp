package com.kleverowl.staffapp.login.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.kleverowl.staffapp.utils.StaffType
import com.kleverowl.staffapp.login.viewmodels.AdminLoginViewModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.activities.BaseActivity
import com.kleverowl.staffapp.components.LoadingProgressBar
import kotlinx.android.synthetic.main.activity_select_staff.*

class SelectStaffActivity : BaseActivity() {

    private lateinit var loadingProgressBar: LoadingProgressBar
    private lateinit var loginModel: AdminLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setContentView(R.layout.activity_select_staff)
        loadingProgressBar = LoadingProgressBar()
        loginModel = ViewModelProvider(this).get(AdminLoginViewModel::class.java)

        staffButton.setOnClickListener {
            loginStaff(StaffType.STAFF)
        }

        deliveryButton.setOnClickListener {
            loginStaff(StaffType.DELIVERY)
        }
    }

    private fun loginStaff(delivery: Int) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("type", delivery)
        startActivity(intent)
    }

}
