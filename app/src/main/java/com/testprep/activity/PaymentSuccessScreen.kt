package com.testprep.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.testprep.R
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_payment_success_screen.*

class PaymentSuccessScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success_screen)

        if (intent.getStringExtra("responseCode").equals("0", ignoreCase = true)) {

            imvSuccessFail!!.setImageResource(R.drawable.success_icon)
            tvMessage.text = "Transaction Successful"
            Utils.ping(this@PaymentSuccessScreen, "Success")

        } else {
            imvSuccessFail!!.setImageResource(R.drawable.fail_icon)
            tvMessage.text = "Transaction Fail"
            Utils.ping(this@PaymentSuccessScreen, "fail")
        }

        Handler().postDelayed(

            /* Runnable
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            {
                // This method will be executed once the timer is over
                // Start your app main activity
                val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

                // close this activity
                finish()
            }, 1500
        )


//        updatePaymentStatus()

    }

}
