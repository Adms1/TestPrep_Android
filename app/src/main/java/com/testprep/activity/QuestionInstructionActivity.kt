package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.testprep.R
import com.testprep.sectionmodule.NewTabQuestionActivity
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
    var totalhint = ""
    var hintused = ""
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
        totalhint = intent.getStringExtra("totalhint")
        hintused = intent.getStringExtra("totalhintused")
//        que_instruction = intent.getStringExtra("que_instruction")

        que_instruction =
            "<html><body style='background-color:clear;'><p style='color:#000000; font-family:Inter-Regular' align=center><font size=4px>" +
                    "<b>Test Instruction</b></font></p><hr><p style='color:#000000; font-family:Inter-Regular'><font size=3px>" +
                    intent.getStringExtra("que_instruction") + "</font></p></body></html>"

        Log.d("queinstruction", que_instruction)

//        DialogUtils.showDialog(this@QuestionInstructionActivity)
//        queinstruction_wb.webViewClient = MyWebViewClient()

        if (que_instruction != "") {

//            que_instruction = intent.getStringExtra("que_instruction")

//            queinstruction_wb.settings.builtInZoomControls = true
//            queinstruction_wb.settings.useWideViewPort = true
//
            queinstruction_wb.settings.javaScriptEnabled = true
//            queinstruction_wb.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            queinstruction_wb.loadDataWithBaseURL("", que_instruction, "text/html", "UTF-8", "")

        } else {

            queinstruction_wb.visibility = View.GONE

        }

//        queinstruction_wb.settings.builtInZoomControls = true
//
//        queinstruction_wb.settings.javaScriptEnabled = true
//        queinstruction_wb.settings.cacheMode = WebSettings.LOAD_NO_CACHE
//        queinstruction_wb.loadUrl(que_instruction)

        queinstruction_header.text = testname

        queinstruction_ivBack.setOnClickListener { onBackPressed() }

        queinstruction_btnStart.setOnClickListener {
            val intent =
                Intent(this@QuestionInstructionActivity, NewTabQuestionActivity::class.java)
            intent.putExtra("testid", testid)
            intent.putExtra("studenttestid", studenttestid)
            intent.putExtra("testname", testname)
            intent.putExtra("testtime", testtime)
            intent.putExtra("totalque", totalque)
            intent.putExtra("subjectname", subjectname)
            intent.putExtra("coursename", coursename)
            intent.putExtra("totalmarks", totalmarks)
            intent.putExtra("tutorname", tutorname)

            intent.putExtra("totalhint", totalhint)
            intent.putExtra("totalhintused", hintused)
            intent.putExtra("que_instruction", que_instruction)

            startActivity(intent)
            finish()
        }

    }

}
