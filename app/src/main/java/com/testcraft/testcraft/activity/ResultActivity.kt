package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.AnswerModel
import com.testcraft.testcraft.sectionmodule.NewTabQuestionActivity
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.Utils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    var resultArr: ArrayList<AnswerModel> = ArrayList()
    var marksCounter = 0.0

    var testid = ""
    var studenttestid = ""

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)

        if (Utils.getStringValue(this@ResultActivity, AppConstants.APP_MODE, "") == AppConstants.DEEPLINK_MODE) {

            Utils.setStringValue(this@ResultActivity, AppConstants.IS_DEEPLINK_STEP, "3")

            if (Utils.getStringValue(this@ResultActivity, AppConstants.IS_LOGIN, "") == "true") {

                result_btnDashboard.visibility = View.VISIBLE
                result_tvReports.visibility = View.VISIBLE
                result_tvReginView.visibility = View.GONE

            } else {
                result_btnDashboard.visibility = View.GONE
                result_tvReports.visibility = View.GONE
                result_tvReginView.visibility = View.VISIBLE
            }

        } else {

            result_btnDashboard.visibility = View.VISIBLE
            result_tvReports.visibility = View.VISIBLE
            result_tvReginView.visibility = View.GONE

        }

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.decorView.systemUiVisibility = View.
        setContentView(R.layout.activity_result)

        connectivity = Connectivity()

        marksCounter = intent.getStringExtra("marks").toDouble()
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

        if (Utils.getStringValue(this@ResultActivity, AppConstants.APP_MODE, "") != AppConstants.NORMAL_MODE) {

            Utils.setStringValue(this@ResultActivity, AppConstants.IS_DEEPLINK_STEP, "3")

            if (Utils.getStringValue(this@ResultActivity, AppConstants.IS_LOGIN, "") == "true") {

                result_btnDashboard.visibility = View.VISIBLE
                result_tvReports.visibility = View.VISIBLE
                result_tvReginView.visibility = View.GONE

            } else {
                result_btnDashboard.visibility = View.GONE
                result_tvReports.visibility = View.GONE
                result_tvReginView.visibility = View.VISIBLE
            }

        } else {

            result_btnDashboard.visibility = View.VISIBLE
            result_tvReports.visibility = View.VISIBLE
            result_tvReginView.visibility = View.GONE

        }

        result_tvMarks.text = """Marks : ${intent.getStringExtra("display_totalmarks")}"""

        result_tvHeading.text = intent.getStringExtra("testname")

        result_btnDashboard.setOnClickListener {
//            if (Utils.getStringValue(this@ResultActivity, AppConstants.APP_MODE, "") == AppConstants.NORMAL_MODE) {
//                CommonWebCalls.callToken(
//                    this@ResultActivity,
//                    "1",
//                    "",
//                    ActionIdData.C2202,
//                    ActionIdData.T2202
//                )

            onBackPressed()
//            } else {
//
//                val intent = Intent(this@ResultActivity, IntroActivity::class.java)
//                startActivity(intent)
//            }
        }

        if (marksCounter <= 0) {
            result_card.background = resources.getDrawable(R.drawable.pink_bg)
            result_tvViewAnswer.setTextColor(resources.getColor(R.color.pink))
            result_tvReports.setTextColor(resources.getColor(R.color.pink))
            result_btnDashboard.setTextColor(resources.getColor(R.color.pink))
            result_tvReginView.setTextColor(resources.getColor(R.color.pink))
            result_tvHeader.text = "Better luck next time"
            dialog_queinfo_tvResult.text = "FAIL"
        } else {
            result_card.background = resources.getDrawable(R.drawable.green_round_bg)
            result_tvViewAnswer.setTextColor(resources.getColor(R.color.green))
            result_tvReports.setTextColor(resources.getColor(R.color.green))
            result_btnDashboard.setTextColor(resources.getColor(R.color.green))
            result_tvReginView.setTextColor(resources.getColor(R.color.green))
            result_tvHeader.text = "Congratulations"
            dialog_queinfo_tvResult.text = "PASS"
        }

        result_tvReginView.setOnClickListener {

            val intent = Intent(this@ResultActivity, IntroActivity::class.java)
            startActivity(intent)

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

        if (Utils.getStringValue(this@ResultActivity, AppConstants.APP_MODE, "") != AppConstants.DEEPLINK_MODE) {

            AppConstants.isFirst = 1
            val intent = Intent(this@ResultActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()

        } else {

        }
    }

}
