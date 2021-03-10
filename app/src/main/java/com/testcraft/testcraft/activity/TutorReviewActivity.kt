package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.TutorReviewAdapter
import com.testcraft.testcraft.models.TutorModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.fragment_tutors_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("SetTextI18n")
class TutorReviewActivity : AppCompatActivity() {

    var tutorid = ""

    var rating = 0F
    var review = ""

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

        setContentView(R.layout.fragment_tutors_review)

        connectivity = Connectivity()

        CommonWebCalls.callToken(
            this@TutorReviewActivity,
            "1",
            "",
            ActionIdData.C2900,
            ActionIdData.T2900
        )

        tutorid = intent.getStringExtra("tutorid")
        tutor_review_header.text = intent.getStringExtra("header")

        tutor_review_btnWritereview.setOnClickListener {

            CommonWebCalls.callToken(
                this@TutorReviewActivity,
                "1",
                "",
                ActionIdData.C2901,
                ActionIdData.T2901
            )

            val dialog = Dialog(this@TutorReviewActivity)
            dialog.setContentView(R.layout.review_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)

            val closeBtn: ImageView = dialog.findViewById(R.id.dialog_review_btnClose)
            val submitBtn: Button = dialog.findViewById(R.id.dialog_review_btnSubmit)
            val etDesc: EditText = dialog.findViewById(R.id.tutor_profile_etReview)
            val ratingbar: RatingBar = dialog.findViewById(R.id.tutor_profile_ratingbar)

            etDesc.setText(review)
            ratingbar.rating = rating

            closeBtn.setOnClickListener { dialog.dismiss() }
            submitBtn.setOnClickListener {

                CommonWebCalls.callToken(
                    this@TutorReviewActivity,
                    "1",
                    "",
                    ActionIdData.C3001,
                    ActionIdData.T3001
                )

                if (ratingbar.rating != 0f) {

                    if (!DialogUtils.isNetworkConnected(this@TutorReviewActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
                        DialogUtils.NetworkDialog(this@TutorReviewActivity)
                        DialogUtils.dismissDialog()
                    }

                    DialogUtils.showDialog(this@TutorReviewActivity)

                    val call = WebClient.buildService().writeRating(
                        WebRequests.writeRatingParams(
                            Utils.getStringValue(
                                this@TutorReviewActivity,
                                AppConstants.USER_ID,
                                "0"
                            )!!, tutorid, etDesc.text.toString(), ratingbar.rating.toString()
                        )
                    )
                    call.enqueue(object : Callback<TutorModel> {
                        override fun onResponse(
                            call: Call<TutorModel>,
                            response: Response<TutorModel>
                        ) {

                            if (response.body() != null) {

                                DialogUtils.dismissDialog()

                                if (response.body()!!.Status == "true") {

                                    dialog.dismiss()
                                    callGetRating()

                                }
                            }
                        }

                        override fun onFailure(call: Call<TutorModel>, t: Throwable) {
                            // Log error here since request failed
                            Log.e("", t.toString())
                            DialogUtils.dismissDialog()
                        }
                    })

                } else {
                    Utils.ping(this@TutorReviewActivity, "Please give rating")
                }
            }

            dialog.show()
        }

        tutor_review_ivBack.setOnClickListener {
            onBackPressed()

        }

        tutor_review_rvReview.layoutManager =
            LinearLayoutManager(this@TutorReviewActivity, LinearLayoutManager.VERTICAL, false)

        callGetRating()

    }

    override fun onBackPressed() {
//        super.onBackPressed()

        AppConstants.isFirst = 15
        val intent = Intent(this@TutorReviewActivity, DashboardActivity::class.java)
        intent.putExtra("tutor_id", tutorid)
        startActivity(intent)
        finish()

    }

    fun callGetRating() {

        if (!DialogUtils.isNetworkConnected(this@TutorReviewActivity)) {
            Utils.ping(this@TutorReviewActivity, AppConstants.NETWORK_MSG)
//            DialogUtils.NetworkDialog(this@TutorReviewActivity)
            val netdialog = Dialog(this@TutorReviewActivity)
            netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            netdialog.setContentView(R.layout.dialog_network)
            netdialog.setCanceledOnTouchOutside(false)

            val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

            btnRetry.setOnClickListener {
                if (DialogUtils.isNetworkConnected(this@TutorReviewActivity)) {
                    netdialog.dismiss()
                    callGetRating()
                }
            }

            netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            netdialog.setCanceledOnTouchOutside(false)
            netdialog.setCancelable(false)
            netdialog.show()
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(this@TutorReviewActivity)

        val call = WebClient.buildService().getTutorRating(tutorid)
        call.enqueue(object : Callback<TutorModel> {

            override fun onResponse(call: Call<TutorModel>, response: Response<TutorModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        tutor_review_tvNoReview.visibility = View.GONE
                        tutor_review_rvReview.visibility = View.VISIBLE

                        for (i in 0 until response.body()!!.data.size) {
                            if (Utils.getStringValue(
                                    this@TutorReviewActivity,
                                    AppConstants.USER_ID,
                                    ""
                                ) == response.body()!!.data[i].StudentID
                            ) {

                                tutor_review_btnWritereview.text = "Edit Review"
                                review = response.body()!!.data[i].Remarks
                                rating = response.body()!!.data[i].Rating.toFloat()

                                break

                            } else {
                                tutor_review_btnWritereview.text = "Write a review"
                                review = ""
                                rating = 0F
                            }
                        }

                        tutor_review_rvReview.adapter =
                            TutorReviewAdapter(this@TutorReviewActivity, response.body()!!.data)

                    } else {

                        tutor_review_tvNoReview.visibility = View.VISIBLE
                        tutor_review_rvReview.visibility = View.GONE

                    }
                }
            }

            override fun onFailure(call: Call<TutorModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

}
