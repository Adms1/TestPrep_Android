package com.testcraft.testcraft

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.sectionmodule.NewTabQuestionActivity
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.Utils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_test_instruction.*
import kotlinx.android.synthetic.main.activity_test_list.*
import kotlinx.android.synthetic.main.dialog_question_information.*

class TestInstructionActivity : AppCompatActivity() {

    var testname = ""
    var testmarks = ""
    var testcourse = ""
    var testque = ""
    var testtutor = ""
    var testsubject = ""

    var testid = ""
    var studenttestid = ""
    var testtime = ""
    var subjectname = ""
    var coursename = ""
    var totalmarks = ""
    var tutorname = ""
    var totalhint = ""
    var hintused = ""
    var que_instruction = ""

    var come_from = ""

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
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

        setContentView(R.layout.activity_test_instruction)

        connectivity = Connectivity()

        testname = intent.getStringExtra("testname")!!
        testque = intent.getStringExtra("totalque")!!
        testsubject = intent.getStringExtra("subjectname")!!
        testcourse = intent.getStringExtra("coursename")!!
        testmarks = intent.getStringExtra("totalmarks")!!
        testtutor = intent.getStringExtra("tutorname")!!

        come_from = intent.getStringExtra("isComeFrom")!!

        testid = intent.getStringExtra("testid")!!
        studenttestid = intent.getStringExtra("studenttestid")!!
        testtime = intent.getStringExtra("testtime")!!
        totalhint = intent.getStringExtra("totalhint")!!
        hintused = intent.getStringExtra("totalhintused")!!
        que_instruction = intent.getStringExtra("que_instruction")!!

        if (AppConstants.IS_SELF_TEST == "true") {
            dialog_testinstruction_tvTeacherName.visibility = View.GONE
            dialog_testinstruction_tvTeacher.visibility = View.GONE
        } else {
            dialog_testinstruction_tvTeacherName.visibility = View.VISIBLE
            dialog_testinstruction_tvTeacher.visibility = View.VISIBLE
        }


        dialog_testinstruction_main_header.text = testname
        dialog_testinstruction_tvHeader.text = testname
        dialog_testinstruction_tvSubjectName.text = testsubject
        dialog_testinstruction_tvCourse.text = testcourse
        dialog_testinstruction_tvTotalMarks.text = testmarks
        dialog_testinstruction_tvQue.text = testque
        dialog_testinstruction_tvTeacherName.text = testtutor

        dialog_testinstruction_tvDismiss.setOnClickListener { finish() }
        dialog_testinstruction_tvStartTest.setOnClickListener {
            CommonWebCalls.callToken(
                this@TestInstructionActivity,
                "1",
                "",
                ActionIdData.C2000,
                ActionIdData.T2000
            )

            val intent =
                Intent(this@TestInstructionActivity, NewTabQuestionActivity::class.java)

            intent.putExtra("isComeFrom", come_from)

            intent.putExtra("testid", testid)
            intent.putExtra("studenttestid", studenttestid)
            intent.putExtra("testname", testname)
            intent.putExtra("testtime", testtime)
            intent.putExtra("totalque", testque)
            intent.putExtra("subjectname", testsubject)
            intent.putExtra("coursename", testcourse)
            intent.putExtra("totalmarks", testmarks)
            intent.putExtra("tutorname", testtutor)

            intent.putExtra("totalhint", totalhint)
            intent.putExtra("totalhintused", hintused)
            intent.putExtra("que_instruction", que_instruction)

            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {

        if (Utils.getStringValue(this@TestInstructionActivity, AppConstants.APP_MODE, "") != AppConstants.DEEPLINK_MODE) {

            when (come_from) {
                "testlist" -> {
                    super.onBackPressed()

                }
                "freetest" -> {

                    AppConstants.isFirst = 1
                    val intent =
                        Intent(this@TestInstructionActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                else       -> {

                }
            }

        } else {

        }
    }

}