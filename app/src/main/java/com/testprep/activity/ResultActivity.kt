package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.testprep.R
import kotlinx.android.synthetic.main.activity_result.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ResultActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.decorView.systemUiVisibility = View.
        setContentView(R.layout.activity_result)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        result_tvViewAnswer.setOnClickListener {
            val intent = Intent(this@ResultActivity, TestReviewActivity::class.java)
            startActivity(intent)
        }

        result_ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}
