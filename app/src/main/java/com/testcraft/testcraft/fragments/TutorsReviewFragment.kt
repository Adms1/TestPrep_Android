package com.testcraft.testcraft.fragments


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.adapter.TutorReviewAdapter
import com.testcraft.testcraft.models.TutorModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import com.testcraft.testcraft.utils.WebRequests
import kotlinx.android.synthetic.main.fragment_tutors_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TutorsReviewFragment : AppCompatActivity() {

    var tutorid = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.fragment_tutors_review)

        tutorid = intent.getStringExtra("tutorid")
        tutor_review_header.text = intent.getStringExtra("header")

        tutor_review_btnWritereview.setOnClickListener {

            val dialog = Dialog(this@TutorsReviewFragment)
            dialog.setContentView(R.layout.review_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)

            val closeBtn: ImageView = dialog.findViewById(R.id.dialog_review_btnClose)
            val submitBtn: Button = dialog.findViewById(R.id.dialog_review_btnSubmit)
            val etDesc: EditText = dialog.findViewById(R.id.tutor_profile_etReview)
            val ratingbar: RatingBar = dialog.findViewById(R.id.tutor_profile_ratingbar)

            closeBtn.setOnClickListener { dialog.dismiss() }
            submitBtn.setOnClickListener {
                if (!DialogUtils.isNetworkConnected(this@TutorsReviewFragment)) {
                    Utils.ping(this@TutorsReviewFragment, "Connetion not available")
                }

                DialogUtils.showDialog(this@TutorsReviewFragment)
                val apiService = WebClient.getClient().create(WebInterface::class.java)

                val call = apiService.writeRating(
                    WebRequests.writeRatingParams(
                        Utils.getStringValue(
                            this@TutorsReviewFragment,
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

            }

            dialog.show()

        }

        tutor_review_ivBack.setOnClickListener {
            //            onBackPressed()

            AppConstants.isFirst = 15
            val intent = Intent(this@TutorsReviewFragment, DashboardActivity::class.java)
            intent.putExtra("tutor_id", tutorid)
            startActivity(intent)

        }

        tutor_review_rvReview.layoutManager =
            LinearLayoutManager(this@TutorsReviewFragment, LinearLayoutManager.VERTICAL, false)

        callGetRating()

    }

    fun callGetRating() {

        if (!DialogUtils.isNetworkConnected(this@TutorsReviewFragment)) {
            Utils.ping(this@TutorsReviewFragment, "Connetion not available")
        }

        DialogUtils.showDialog(this@TutorsReviewFragment)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getTutorRating(tutorid)
        call.enqueue(object : Callback<TutorModel> {
            override fun onResponse(call: Call<TutorModel>, response: Response<TutorModel>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.Status == "true") {

                        tutor_review_tvNoReview.visibility = View.GONE
                        tutor_review_rvReview.visibility = View.VISIBLE

                        tutor_review_rvReview.adapter =
                            TutorReviewAdapter(this@TutorsReviewFragment, response.body()!!.data)

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
