package com.testcraftdemo.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.testcraftdemo.R
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.activity_signup.*

class OtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        otp_btnSubmit.setOnClickListener {

            otp_tvHeading.visibility = View.GONE
            otp_tvInstruction.visibility = View.GONE
            otp_etOtp.visibility = View.GONE
            otp_btnSubmit.visibility = View.GONE
            otp_ivSuccess.visibility = View.VISIBLE
            otp_tvSuccess.visibility = View.VISIBLE

//            val intent = Intent(this@OtpActivity, DashboardActivity::class.java)
//            startActivity(intent)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
