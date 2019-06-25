package com.testprep.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.testprep.R
import kotlinx.android.synthetic.main.activity_select_compititive.*

class SelectCompititiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_select_compititive)

        competitive_ivBack.setOnClickListener {
            onBackPressed()
        }

        pref_btnNo.setOnClickListener {
            val intent = Intent(this@SelectCompititiveActivity, DashboardActivity::class.java)
            startActivity(intent)
        }

        pref_btnYes.setOnClickListener {
            val intent = Intent(this@SelectCompititiveActivity, DashboardActivity::class.java)
            startActivity(intent)
        }

    }
}
