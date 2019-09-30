package com.testprep.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.testprep.R
import com.testprep.utils.DialogUtils
import kotlinx.android.synthetic.main.activity_view_invoice.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class ViewInvoiceActivity : AppCompatActivity() {

    var url = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_view_invoice)

        DialogUtils.showDialog(this@ViewInvoiceActivity)
        invoice_view.webViewClient = MyWebViewClient()

        invoice_header.text = intent.getStringExtra("header")

        if (intent.hasExtra("url")) {
            url = intent.getStringExtra("url")
        }

        invoice_view.settings.builtInZoomControls = true

        invoice_view.settings.javaScriptEnabled = false
        invoice_view.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        invoice_view.loadUrl(url)

//        invoice_view.loadUrl(
//            "https://docs.google.com/viewerng/viewer?url=http://admin.testcraft.in:8090/tutor/purchasePackages/detail/" + intent.getStringExtra(
//                "invoice_id"
//            )
//        )

        invoice_ivBack.setOnClickListener { finish() }
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

//    inner class MyWebViewClient : WebChromeClient() {
//
//        override fun onProgressChanged(view: WebView, progress: Int) {
//            //Also you can show the progress percentage using integer value 'progress'
//            if (!prDialog.isShowing()) {
//                prDialog = ProgressDialog.show(this@Grades, null, "loading, please wait...")
//            }
//
//
//            if (progress == 100 && prDialog.isShowing()) {
//                prDialog.dismiss()
//            }
//        }
//
//
//    }

//    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//        view.loadUrl(url)
//        return true
//    }
//
//    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//        super.onPageStarted(view, url, favicon)
//        DialogUtils.showDialog( this@ViewInvoiceActivity)
//    }
//
//    override fun onPageFinished(view: WebView, url: String) {
//        super.onPageFinished(view, url)
//        DialogUtils.dismissDialog()
//    }

}
