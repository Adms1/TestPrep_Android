package com.testcraft.testcraft.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.activity.StrengthWeeknessActivity
import com.testcraft.testcraft.activity.ViewInvoiceActivity
import com.testcraft.testcraft.adapter.QuestionAttemptAdapter
import com.testcraft.testcraft.models.AttemptModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import kotlinx.android.synthetic.main.activity_test_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("SetTextI18n")
class TestReviewFragment : Fragment() {

    var testid = ""
    var studenttestid = ""
    var totalque = ""
    var isCompetitive: Int? = null

    var testname = ""

    var bundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_test_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C2300, ActionIdData.T2300)

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

        testid = bundle!!.getString("testid")!!
        studenttestid = bundle!!.getString("studenttestid")!!
        testname = bundle!!.getString("testname")!!

//        testid = intent.getStringExtra("testid")
//        studenttestid = intent.getStringExtra("studenttestid")

//        review_ivBack.setOnClickListener {
        //            AppConstants.isFirst = 0
//            val intent = Intent(activity!!, DashboardActivity::class.java)
//            startActivity(intent)
//            finish()
//            onBackPressed()

//        }

//        if (AppConstants.IS_SELF_TEST == "true") {
//            review_tvRank.visibility = View.VISIBLE
//            rlRank.visibility = View.VISIBLE
//        } else {
//            review_tvRank.visibility = View.GONE
//            rlRank.visibility = View.GONE
//        }

        review_ivInfo.setOnClickListener {
            callSubjectwisemarks()
        }

        review_btnReport.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C2302, ActionIdData.T2302)

            //            if (isCompetitive == 0) {
//                val reportdialog = Dialog(activity)
//                reportdialog.setContentView(R.layout.dialog_report_issue)
//                reportdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                reportdialog.setCanceledOnTouchOutside(false)
//
//                val ivIssue: ImageView = reportdialog.findViewById(R.id.queTab_ivIssue)
//                val tvHeader: TextView = reportdialog.findViewById(R.id.dialog_report_tvHeader)
//                val close: TextView = reportdialog.findViewById(R.id.dialog_report_tvClose)
//                val queproblem: TextView = reportdialog.findViewById(R.id.dialog_report_tvQueProblem)
//                val ansproblem: TextView = reportdialog.findViewById(R.id.dialog_report_tvAnsProblem)
//                val hintexplanation: TextView = reportdialog.findViewById(R.id.dialog_report_tvHintProblem)
//                val line: View = reportdialog.findViewById(R.id.dialog_report_line2)
//
//                tvHeader.text = getString(R.string.test_summary_report)
//                hintexplanation.text = "Chpater wise"
//                ansproblem.text = "Topic wise"
//                queproblem.visibility = View.GONE
//                ivIssue.visibility = View.GONE
//                line.visibility = View.GONE
//
//                ansproblem.setOnClickListener {
//                    val intent = Intent(context, ViewInvoiceActivity::class.java)
//                    intent.putExtra("header", getString(R.string.test_summary_report))
//                    intent.putExtra("url", AppConstants.SUMMARY_REPORT_URL + "STID=" + studenttestid)
//                    startActivity(intent)
//                }
//
//                hintexplanation.setOnClickListener {
            val intent = Intent(context, ViewInvoiceActivity::class.java)
            intent.putExtra("header", getString(R.string.test_summary_report))
            intent.putExtra(
                "url",
                AppConstants.SUMMARY_REPORT_URL + "STID=" + studenttestid + "&CHAP=1"
            )
            startActivity(intent)
//                }

//                close.setOnClickListener {
//                    reportdialog.dismiss()
//                }
//
//                reportdialog.show()
//
//            } else {
//                val intent = Intent(context, ViewInvoiceActivity::class.java)
//                intent.putExtra("header", getString(R.string.test_summary_report))
//                intent.putExtra("url", AppConstants.SUMMARY_REPORT_URL + "STID=" + studenttestid)
//                startActivity(intent)
//            }

        }

        review_btnSubmit.setOnClickListener {

            CommonWebCalls.callToken(activity!!, "1", "", ActionIdData.C2301, ActionIdData.T2301)

            AppConstants.isFirst = 9
            val bundle = Bundle()
            bundle.putString("testid", testid)
            bundle.putString("studenttestid", studenttestid)
            bundle.putString("totalque", totalque)
            bundle.putString("testname", testname)
            setFragments(bundle)

//            val intent = Intent(activity!!, ViewSolutionActivity::class.java)
//            intent.putExtra("testid", testid)
//            intent.putExtra("studenttestid", studenttestid)
//            startActivity(intent)
        }

        rlRank.setOnClickListener {

            val intent = Intent(activity!!, StrengthWeeknessActivity::class.java)
            intent.putExtra("studenttestid", studenttestid)
            startActivity(intent)

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

                    totalque =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TotalQue")
                            .asString

                    isCompetitive =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("IsCompetetive").asInt

                    review_tvPercentage.text =
                        "Percentage : " + response.body()!!.get("data").asJsonArray[0].asJsonObject.get("percentage").asString + "%"
                    review_tvPercentile.text =
                        "Percentile : " + response.body()!!.get("data").asJsonArray[0].asJsonObject.get("Percentile").asString

                    if (AppConstants.IS_SELF_TEST == "true") {

                        review_ivInfo.visibility = View.GONE
                        rlRank.visibility = View.GONE
                        review_tvRank.visibility = View.GONE
                        review_tvPercentile.visibility = View.GONE

                    } else {
                        if (isCompetitive == 1) {
                            review_ivInfo.visibility = View.VISIBLE
                            rlRank.visibility = View.VISIBLE

                            review_tvRank.visibility = View.VISIBLE

                            if (response.body()!!.get("data").asJsonArray[0].asJsonObject.get("AIR").asString != "N/A" && response.body()!!.get("data").asJsonArray[0].asJsonObject.get("AIR").asString != "") {
                                review_tvRank.text =
                                    "AIR : " + response.body()!!.get("data").asJsonArray[0].asJsonObject.get("AIR").asString
                            } else {
                                review_tvRank.text =
                                    "Internal Rank : " + response.body()!!.get("data").asJsonArray[0].asJsonObject.get("InternalRank").asString
                            }

                        } else {

                            review_tvRank.text =
                                "Internal Rank : " + response.body()!!.get("data").asJsonArray[0].asJsonObject.get("InternalRank").asString

                            review_ivInfo.visibility = View.GONE
                            rlRank.visibility = View.GONE
                            review_tvRank.visibility = View.VISIBLE

                        }
                    }

                    review_tvCorrect.text =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("Correct").asString
                    review_tvIncorrect.text =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("Wrong").asString
                    review_tvUnanswered.text =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("UnAnswered").asString
                    review_ivCorrect.text =
                        response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TotalGetMarks").asString + "\n______\n" + response.body()!!.get(
                            "data"
                        ).asJsonArray[0].asJsonObject.get("TotalMarks").asString

                    review_tvTotalQue.text =
                        "Total Questions : " + response.body()!!.get("data").asJsonArray[0].asJsonObject.get(
                            "TotalQue"
                        ).asString

                } else {

                    Toast.makeText(
                        activity!!,
                        response.body()!!.get("Msg").asString,
                        Toast.LENGTH_LONG
                    )
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

    fun callSubjectwisemarks() {

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getSubjectwiseMarks(studenttestid)
        call.enqueue(object : Callback<AttemptModel> {

            override fun onResponse(call: Call<AttemptModel>, response: Response<AttemptModel>) {

                if (response.body()!!.Status == "true") {

                    val dialog = Dialog(activity!!)

                    if (!dialog.isShowing) {
                        dialog.setContentView(R.layout.dialog_que_attempt_report)
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.setCanceledOnTouchOutside(false)

                        val header: TextView = dialog.findViewById(R.id.attempt_tvHeader)
                        val btnCancel: TextView = dialog.findViewById(R.id.attempt_btnClose)
                        val btnOk: TextView = dialog.findViewById(R.id.attempt_tvOK)
                        val rvList: RecyclerView = dialog.findViewById(R.id.attempt_rvList)

                        rvList.layoutManager =
                            LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

                        rvList.adapter =
                            QuestionAttemptAdapter(activity!!, "review", response.body()!!.data)

                        btnOk.text = "     OK     "
                        btnCancel.visibility = View.GONE
                        header.text = "Subjectwise Marks"

                        btnOk.setOnClickListener {
                            dialog.dismiss()
                        }

                        dialog.show()
                    }

                } else {

                    Toast.makeText(activity!!, response.body()!!.Msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AttemptModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }


}
