package com.kleverowl.staffapp.login.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.kleverowl.staffapp.utils.StaffType
import com.kleverowl.staffapp.login.viewmodels.AdminLoginViewModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.activities.BaseActivity
import com.kleverowl.staffapp.components.LoadingProgressBar
import com.kleverowl.staffapp.home.views.HomeActivity
import com.kleverowl.staffapp.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private var type: Int = 0
    private lateinit var loadingProgressBar: LoadingProgressBar
    private lateinit var loginModel: AdminLoginViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        setContentView(R.layout.activity_login)

        type = intent.extras?.getInt("type") ?: StaffType.STAFF
        loadingProgressBar = LoadingProgressBar()
        loginModel = ViewModelProvider(this).get(AdminLoginViewModel::class.java)

        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (areCredentialsValid()) {
            loadingProgressBar.showProgress(this, "")
            loginModel.login(type, phoneNumberEditText.text.toString(), keyEditText.text.toString())
                .observe(this, Observer { loginSuccess ->
                    loadingProgressBar.hideProgress()
                    if (loginSuccess) {

                        auth.signInAnonymously()
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this, HomeActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                    Toast.makeText(
                                        this@LoginActivity,
                                        getString(R.string.login_success),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        getString(R.string.login_failure),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }


                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.login_failure),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }
    }

    private fun areCredentialsValid(): Boolean {

        if (keyEditText.text.toString().isEmpty()) {
            keyEditText.error = getString(R.string.required)
            return false
        }

        if (phoneNumberEditText.text.toString().isEmpty()) {
            phoneNumberEditText.error = getString(R.string.required)
            return false
        }

        if (!Utils.isValidPhoneNumber(phoneNumberEditText.text.toString())) {
            Toast.makeText(this, getString(R.string.invalid_phone_number), Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}
