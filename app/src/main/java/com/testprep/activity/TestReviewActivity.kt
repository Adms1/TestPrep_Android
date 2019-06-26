package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.testprep.R
import kotlinx.android.synthetic.main.activity_test_review.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class TestReviewActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_test_review)

        review_ivBack.setOnClickListener { onBackPressed() }

        review_btnSubmit.setOnClickListener {
            val intent = Intent(this@TestReviewActivity, ViewSolutionActivity::class.java)
            startActivity(intent)
        }

    }
}
