package com.testprep.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import kotlinx.android.synthetic.main.activity_test_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class TestReviewActivity : AppCompatActivity() {

    var testid = ""
    var studenttestid = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_test_review)

        testid = intent.getStringExtra("testid")
        studenttestid = intent.getStringExtra("studenttestid")

        review_ivBack.setOnClickListener {
//            AppConstants.isFirst = 0
//            val intent = Intent(this@TestReviewActivity, DashboardActivity::class.java)
//            startActivity(intent)
//            finish()
            onBackPressed()

        }

        review_btnSubmit.setOnClickListener {
            val intent = Intent(this@TestReviewActivity, ViewSolutionActivity::class.java)
            intent.putExtra("testid", testid)
            intent.putExtra("studenttestid", studenttestid)
            startActivity(intent)
        }

        callSubmitAPI()

    }

    override fun onBackPressed() {
        finish()
    }

    fun callSubmitAPI() {
        val sortDialog = Dialog(this@TestReviewActivity)//,R.style.PauseDialog);//, R.style.PauseDialog);
        val window = sortDialog.window
        val wlp = window!!.attributes
        sortDialog.window!!.attributes.verticalMargin = 0.10f
        wlp.gravity = Gravity.BOTTOM
        window.attributes = wlp

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sortDialog.setCancelable(true)
        sortDialog.show()

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getAnalyse(intent.getStringExtra("studenttestid"))
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                sortDialog.dismiss()

                if (response.body()!!.get("Status").asString == "true") {

                    review_tvCorrect.text =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("Correct").asString + "    Correct"
                    review_tvIncorrect.text =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("Wrong").asString + "    Incorrect"
                    review_tvUnanswered.text =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("UnAnswered").asString + "    Unanswered"
                    review_ivCorrect.text =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TotalGetMarks").asString + "\n______\n" + response.body()!!.get(
                            "data"
                        ).asJsonArray[0].asJsonObject.get("TotalMarks").asString

                } else {

                    Toast.makeText(this@TestReviewActivity, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                sortDialog.dismiss()
            }
        })
    }

}
