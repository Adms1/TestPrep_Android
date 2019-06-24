package com.testprep.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.testprep.R
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_otp.*

class OtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_otp)

        otp_ivBack.setOnClickListener { onBackPressed() }

        otp_btnSubmit.setOnClickListener {

            if (otp_btnSubmit.text.toString() != "Done") {
                Toast.makeText(this@OtpActivity, "Registration Successfully done...", Toast.LENGTH_LONG).show()

                Utils.hideKeyboard(this@OtpActivity)
                otp_tvVerificationSuccess.visibility = View.VISIBLE
                otp_tvHeading.text = "Awesome!"

                otp_tvInstruction.visibility = View.GONE
                otp_btnSubmit.text = "Done"
                otp_tvResend.visibility = View.GONE
                otp_etOtp.visibility = View.GONE
//                Handler().postDelayed(
//
//                    {
//                        val intent = Intent(this@OtpActivity, NewActivity::class.java)
//                        startActivity(intent)
//
//                        // close this activity
//                        finish()
//                    }, 1000
//                )
            } else {


            }
        }
    }
}
