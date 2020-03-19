package com.testcraft.testcraft.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.NotificationAdapter
import kotlinx.android.synthetic.main.activity_notification.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class NotificationActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_notification)

        notification_rvList.layoutManager =
            LinearLayoutManager(this@NotificationActivity, LinearLayoutManager.VERTICAL, false)

        notification_rvList.adapter = NotificationAdapter(this@NotificationActivity)

        notification_ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}
