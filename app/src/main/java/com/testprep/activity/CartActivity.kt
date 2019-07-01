package com.testprep.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.testprep.R
import com.testprep.adapter.CartAdapter
import kotlinx.android.synthetic.main.activity_cart.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class CartActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_cart)

        cart_ivBack.setOnClickListener { onBackPressed() }

        cart_rvList.layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)

        cart_rvList.adapter = CartAdapter(this@CartActivity)

    }
}
