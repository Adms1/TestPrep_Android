package com.testprep.activity

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.activity.DashboardActivity.Companion.setFragments
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import kotlinx.android.synthetic.main.activity_test_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestReviewActivity : Fragment() {

    var testid = ""
    var studenttestid = ""

    var bundle: Bundle? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.activity_test_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

//        setContentView(R.layout.activity_test_review)

        bundle = this.arguments

        testid = bundle!!.getString("testid")
        studenttestid = bundle!!.getString("studenttestid")

//        testid = intent.getStringExtra("testid")
//        studenttestid = intent.getStringExtra("studenttestid")

        review_ivBack.setOnClickListener {
//            AppConstants.isFirst = 0
//            val intent = Intent(activity!!, DashboardActivity::class.java)
//            startActivity(intent)
//            finish()
//            onBackPressed()

        }

        review_btnSubmit.setOnClickListener {

            AppConstants.isFirst = 9
            val bundle = Bundle()
            bundle.putString("testid", testid)
            bundle.putString("studenttestid", studenttestid)
            setFragments(bundle)

//            val intent = Intent(activity!!, ViewSolutionActivity::class.java)
//            intent.putExtra("testid", testid)
//            intent.putExtra("studenttestid", studenttestid)
//            startActivity(intent)
        }

        callSubmitAPI()

    }

//    override fun onBackPressed() {
//        finish()
//    }

    fun callSubmitAPI() {
        val sortDialog = Dialog(activity!!)//,R.style.PauseDialog);//, R.style.PauseDialog);
        val window = sortDialog.window
        val wlp = window!!.attributes
        sortDialog.window!!.attributes.verticalMargin = 0.10f
        wlp.gravity = Gravity.BOTTOM
        window.attributes = wlp

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sortDialog.setCancelable(true)
        sortDialog.show()

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getAnalyse(studenttestid)
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

                    Toast.makeText(activity!!, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG)
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
