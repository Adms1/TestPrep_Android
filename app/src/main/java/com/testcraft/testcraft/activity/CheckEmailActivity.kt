package com.testcraft.testcraft.activity

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import kotlinx.android.synthetic.main.activity_check_email.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class CheckEmailActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_check_email)

        connectivity = Connectivity()

        checkmail_ivBack.setOnClickListener { onBackPressed() }

    }
}
