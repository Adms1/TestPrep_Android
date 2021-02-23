package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.utils.DialogUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_payment_view.*

class PaymentViewActivity : AppCompatActivity() {
    var url = ""

    var connectivity: Connectivity? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_payment_view)

        if (intent.hasExtra("url")) {

            DialogUtils.showDialog(this@PaymentViewActivity)
            paymentinvoice_view.webViewClient = MyWebViewClient()

            paymentinvoice_view.visibility = View.VISIBLE

            url = intent.getStringExtra("url")!!

            Log.d("payment url", "URL : $url")

            paymentinvoice_view.settings.builtInZoomControls = true
            paymentinvoice_view.settings.useWideViewPort = true

            paymentinvoice_view.settings.javaScriptEnabled = true
            paymentinvoice_view.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            paymentinvoice_view.loadUrl(url)

        } else {

            paymentinvoice_view.visibility = View.GONE

        }

        paymentinvoice_ivBack.setOnClickListener { finish() }
    }

    inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // TODO Auto-generated method stub
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            DialogUtils.dismissDialog()
        }
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK && paymentinvoice_view.canGoBack()) {
////            paymentinvoice_view.goBack()
//            finish()
//            return true
//        } else {
//            finish()
//        }
//        return super.onKeyDown(keyCode, event)
//    }

}