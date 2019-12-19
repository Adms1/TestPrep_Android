package com.testcraft.testcraft.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.AnswerModel
import com.testcraft.testcraft.sectionmodule.NewTabQuestionActivity
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import kotlinx.android.synthetic.main.activity_result.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ResultActivity : AppCompatActivity() {

    var resultArr: ArrayList<AnswerModel> = ArrayList()
//    var marksCounter = 0

    var testid = ""
    var studenttestid = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.decorView.systemUiVisibility = View.
        setContentView(R.layout.activity_result)

        testid = intent.getStringExtra("testid")
        studenttestid = intent.getStringExtra("studenttestid")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        resultArr = NewTabQuestionActivity.ansArr
        Log.d("arrresult", "" + resultArr)

//        for (i in 0 until resultArr.size) {
//            if (resultArr[i].ansresult) {
//                marksCounter += 1
//            }
//        }

        result_tvMarks.text = "Marks : " + intent.getStringExtra("display_totalmarks")
        result_tvHeading.text = intent.getStringExtra("testname")

        result_btnDashboard.setOnClickListener {

            CommonWebCalls.callToken(
                this@ResultActivity,
                "1",
                "",
                ActionIdData.C2202,
                ActionIdData.T2202
            )

            onBackPressed()
        }

        if (intent.getStringExtra("marks") <= "0") {
            result_card.background = resources.getDrawable(R.drawable.pink_bg)
            result_tvViewAnswer.setTextColor(resources.getColor(R.color.pink))
            result_tvReports.setTextColor(resources.getColor(R.color.pink))
            result_btnDashboard.setTextColor(resources.getColor(R.color.pink))
            result_tvHeader.text = "Better luck next time"
            dialog_queinfo_tvResult.text = "FAIL"
        } else {
            result_card.background = resources.getDrawable(R.drawable.green_round_bg)
            result_tvViewAnswer.setTextColor(resources.getColor(R.color.green))
            result_tvReports.setTextColor(resources.getColor(R.color.green))
            result_btnDashboard.setTextColor(resources.getColor(R.color.green))
            result_tvHeader.text = "Congratulations"
            dialog_queinfo_tvResult.text = "PASS"
        }

        result_tvViewAnswer.setOnClickListener {
            //            val intent = Intent(this@ResultActivity, ViewSolutionActivity::class.java)
//            intent.putExtra("testid", testid)
//            intent.putExtra("studenttestid", studenttestid)
//            startActivity(intent)
        }

        result_tvReports.setOnClickListener {

            CommonWebCalls.callToken(
                this@ResultActivity,
                "1",
                "",
                ActionIdData.C2201,
                ActionIdData.T2201
            )

            AppConstants.isFirst = 10

//            setFragments(bundle)
            val intent1 = Intent(this@ResultActivity, DashboardActivity::class.java)
            intent1.putExtra("testid", testid)
            intent1.putExtra("studenttestid", studenttestid)
            intent1.putExtra("testname", result_tvHeading.text.toString())
            startActivity(intent1)
            finish()
        }

        result_ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        AppConstants.isFirst = 1
        val intent = Intent(this@ResultActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

}
