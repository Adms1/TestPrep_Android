package com.testprep.fragments


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.testprep.R
import com.testprep.adapter.TutorReviewAdapter
import kotlinx.android.synthetic.main.fragment_tutors_review.*
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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.fragment_tutors_review)

        tutor_review_header.text = intent.getStringExtra("header")

        tutor_review_btnWritereview.setOnClickListener {
            val dialog = Dialog(this@TutorsReviewFragment)
            dialog.setContentView(R.layout.review_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)

            val closeBtn: View = dialog.findViewById(R.id.dialog_review_btnClose)
            val submitBtn: View = dialog.findViewById(R.id.dialog_review_btnSubmit)

            closeBtn.setOnClickListener { dialog.dismiss() }
            submitBtn.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }

        tutor_review_ivBack.setOnClickListener { onBackPressed() }

        tutor_review_rvReview.layoutManager =
            LinearLayoutManager(this@TutorsReviewFragment, LinearLayoutManager.VERTICAL, false)

        tutor_review_rvReview.adapter = TutorReviewAdapter(this@TutorsReviewFragment)

    }

}
