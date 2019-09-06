package com.testprep.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.testprep.R
import kotlinx.android.synthetic.main.activity_view_invoice.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ViewInvoiceActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_view_invoice)

        invoice_view.loadUrl(
            "https://docs.google.com/viewerng/viewer?url=http://admin.testcraft.in:8090/tutor/purchasePackages/detail/" + intent.getStringExtra(
                "invoice_id"
            )
        )
        invoice_view.settings.builtInZoomControls = true

        invoice_ivBack.setOnClickListener { finish() }
    }
}
