package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.testprep.R
import com.testprep.models.AnswerModel
import kotlinx.android.synthetic.main.activity_result.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ResultActivity : AppCompatActivity() {

    var resultArr: ArrayList<AnswerModel> = ArrayList()
//    var marksCounter = 0

    var testid = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.decorView.systemUiVisibility = View.
        setContentView(R.layout.activity_result)

        testid = intent.getStringExtra("testid")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        resultArr = TabwiseQuestionActivity.ansArr
        Log.d("arrresult", "" + resultArr)

//        for (i in 0 until resultArr.size) {
//            if (resultArr[i].ansresult) {
//                marksCounter += 1
//            }
//        }
        result_tvMarks.text = "Marks : " + intent.getStringExtra("marks")

        if (intent.getStringExtra("marks") <= "0") {
            result_card.background = resources.getDrawable(R.drawable.pink_bg)
            result_tvViewAnswer.setTextColor(resources.getColor(R.color.pink))
            result_tvHeader.text = "Better luck next time"
            dialog_queinfo_tvResult.text = "FAIL"
        } else {
            result_card.background = resources.getDrawable(R.drawable.green_round_bg)
            result_tvViewAnswer.setTextColor(resources.getColor(R.color.green))
            result_tvHeader.text = "Congratulations"
            dialog_queinfo_tvResult.text = "PASS"
        }

        result_tvViewAnswer.setOnClickListener {
            val intent = Intent(this@ResultActivity, ViewSolutionActivity::class.java)
            intent.putExtra("testid", testid)
            startActivity(intent)
        }

//        result_tvReports.setOnClickListener {
//            val intent = Intent(this@ResultActivity, TestReviewActivity::class.java)
//            intent.putExtra("testid", testid)
//            startActivity(intent)
//        }

        result_ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}
