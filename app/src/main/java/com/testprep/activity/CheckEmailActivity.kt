package com.testprep.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.testprep.R
import kotlinx.android.synthetic.main.activity_check_email.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class CheckEmailActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_check_email)

        checkmail_ivBack.setOnClickListener { onBackPressed() }

    }
}
