package com.kleverowl.staffapp.home.views

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.kleverowl.staffapp.models.OrderModel
import com.kleverowl.staffapp.R
import com.kleverowl.staffapp.activities.SplashActivity
import com.kleverowl.staffapp.orders.views.NewOrderFragment
import com.kleverowl.staffapp.orders.views.OngoingOrderFragment
import com.kleverowl.staffapp.orders.views.OrderFragment
import com.kleverowl.staffapp.orders.views.PastOrderFragment
import com.kleverowl.staffapp.utils.AppConstants
import com.kleverowl.staffapp.utils.PreferenceConnector
import com.kleverowl.staffapp.utils.PreferenceConnector.TYPE
import com.kleverowl.staffapp.utils.PreferenceConnector.USER_DETAILS
import com.kleverowl.staffapp.utils.StaffType
import kotlinx.android.synthetic.main.confirmation_dialog.view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity(),
    OrderFragment.OrderFragmentAttachedListener, NewOrderFragment.NewOrderFragmentAttachedListener,
    OngoingOrderFragment.OnGoingFragmentAttachedListener,
    PastOrderFragment.PastOrderFragmentAttachedListener {


    private var type: Int = StaffType.STAFF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = ""

        type = PreferenceConnector.readInteger(this, TYPE, StaffType.STAFF)


        toolbarTitle.text = getString(R.string.your_orders)
        logoutButton.setOnClickListener {
            PreferenceConnector.remove(TYPE, this)
            PreferenceConnector.remove(USER_DETAILS, this)
            if (type == StaffType.STAFF)
                FirebaseMessaging.getInstance().unsubscribeFromTopic("staff") else
                FirebaseMessaging.getInstance().unsubscribeFromTopic("delivery")
            FirebaseAuth.getInstance().signOut()
            navigateBackToSplash()
        }





        if (type == StaffType.STAFF) {
            FirebaseMessaging.getInstance().subscribeToTopic("staff")
                .addOnCompleteListener {
                }

        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("delivery")
                .addOnCompleteListener {
                }
        }


        val fragment = OrderFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        fragmentTransaction.replace(R.id.container, fragment, "OrderFragment").commit()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(AppConstants.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun navigateBackToSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onGoingOrderClicked(orderModel: OrderModel) {

    }

    override fun onOrderClicked(orderModel: OrderModel) {

    }

    override fun onNewOrderClicked(orderModel: OrderModel) {
    }

    override fun onCallClicked(orderModel: OrderModel) {
        orderModel.customerMobileNumber?.let {
            showConfirmationDialog(it)
        }
    }

    private fun showConfirmationDialog(phoneNumber: String) {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        //Now we need an AlertDialog.Builder object
        val builder = AlertDialog.Builder(this)
        //then we will inflate the custom alert dialog xml that we created
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.confirmation_dialog, viewGroup, false)


        dialogView.msgTextView.text = getString(R.string.make_call_text)


        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView)

        //finally creating the alert dialog and displaying it
        val alertDialog = builder.create()

        dialogView.noButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.yesButton.setOnClickListener {
            alertDialog.dismiss()
            makePhoneCall(phoneNumber)
        }


        alertDialog.show()
    }

    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        startActivity(intent)
    }

}
