package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.testprep.R
import com.testprep.sectionmodule.NewTabQuestionActivity
import com.testprep.utils.DialogUtils
import kotlinx.android.synthetic.main.activity_question_instruction.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class QuestionInstructionActivity : AppCompatActivity() {

    var testid = ""
    var studenttestid = ""
    var testname = ""
    var testtime = ""
    var totalque = ""
    var subjectname = ""
    var coursename = ""
    var totalmarks = ""
    var tutorname = ""
    var que_instruction = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_question_instruction)

        testid = intent.getStringExtra("testid")
        studenttestid = intent.getStringExtra("studenttestid")
        testname = intent.getStringExtra("testname")
        testtime = intent.getStringExtra("testtime")
        totalque = intent.getStringExtra("totalque")
        subjectname = intent.getStringExtra("subjectname")
        coursename = intent.getStringExtra("coursename")
        totalmarks = intent.getStringExtra("totalmarks")
        tutorname = intent.getStringExtra("tutorname")
        que_instruction = intent.getStringExtra("que_instruction")

        DialogUtils.showDialog(this@QuestionInstructionActivity)
        queinstruction_wb.webViewClient = MyWebViewClient()

        queinstruction_wb.visibility = View.VISIBLE

        queinstruction_wb.settings.builtInZoomControls = true

        queinstruction_wb.settings.javaScriptEnabled = false
        queinstruction_wb.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        queinstruction_wb.loadUrl(que_instruction)

        queinstruction_header.text = testname

        queinstruction_ivBack.setOnClickListener { onBackPressed() }

        queinstruction_btnStart.setOnClickListener {
            val intent = Intent(this@QuestionInstructionActivity, NewTabQuestionActivity::class.java)
            intent.putExtra("testid", testid)
            intent.putExtra("studenttestid", studenttestid)
            intent.putExtra("testname", testname)
            intent.putExtra("testtime", testtime)
            intent.putExtra("totalque", totalque)
            intent.putExtra("subjectname", subjectname)
            intent.putExtra("coursename", coursename)
            intent.putExtra("totalmarks", totalmarks)
            intent.putExtra("tutorname", tutorname)
            startActivity(intent)
            finish()
        }

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

}
