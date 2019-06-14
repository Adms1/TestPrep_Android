package com.testprep.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
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

        otp_btnSubmit.setOnClickListener {

            Toast.makeText(this@OtpActivity, "Registration Suceessfully...", Toast.LENGTH_LONG).show()

            Utils.hideKeyboard(this@OtpActivity)

            otp_tvHeading.visibility = View.GONE
            otp_tvInstruction.visibility = View.GONE
            otp_etOtp.visibility = View.GONE
            otp_btnSubmit.visibility = View.GONE
            otp_ivSuccess.visibility = View.VISIBLE
            otp_tvSuccess.visibility = View.VISIBLE
            otp_ivLogo.visibility = View.GONE

//            val intent = Intent(this@OtpActivity, DashboardActivity::class.java)
//            startActivity(intent)

            Handler().postDelayed(

                /* Runnable
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    val intent = Intent(this@OtpActivity, NewActivity::class.java)
                    startActivity(intent)
//                    overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

                    // close this activity
                    finish()
                }, 1000
            )


        }
    }


}
