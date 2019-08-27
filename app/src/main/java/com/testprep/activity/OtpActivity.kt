package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class OtpActivity : AppCompatActivity() {

    var otp = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_otp)

        otp = intent.getStringExtra("otp")

        otp_ivBack.setOnClickListener { onBackPressed() }

        otp_tvInstruction.text =
            "Please enter verification code \n sent to +91 " + intent.getStringExtra("mobile_number")

        otp_tvResend.setOnClickListener {
            Toast.makeText(this@OtpActivity, "OTP has been sent to your registered mobile number", Toast.LENGTH_LONG)
                .show()
            callResend()

        }

        otp_btnSubmit.setOnClickListener {

            if (otp_btnSubmit.text.toString() != "Done") {

                if (otp_etOtp.value.toString() == otp) {

                    Utils.hideKeyboard(this@OtpActivity)
                    otp_tvVerificationSuccess.visibility = View.VISIBLE
                    otp_tvHeading.text = "Awesome!"

                    otp_tvInstruction.visibility = View.GONE
                    otp_btnSubmit.text = "Done"
                    otp_tvResend.visibility = View.GONE
                    otp_etOtp.visibility = View.GONE

                    otp_ivLogo.setImageDrawable(resources.getDrawable(R.drawable.success_verification_icn))
                } else {
                    Toast.makeText(this@OtpActivity, "OTP does not match", Toast.LENGTH_LONG).show()
                }
//                Handler().postDelayed(
//                    {
//                        val intent = Intent(this@OtpActivity, NewActivity::class.java)
//                        startActivity(intent)
//
//                        // close this activity
//                        finish()
//                    }, 1000
//                )
            } else {

                if (intent.getStringExtra("come_from") == "forgot password") {
                    val intent = Intent(this@OtpActivity, ChangePasswordActivity::class.java)
                    intent.putExtra("come_from", "otp")
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@OtpActivity, NewActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }
    }

    fun callResend() {

        if (!DialogUtils.isNetworkConnected(this@OtpActivity)) {
            Utils.ping(this@OtpActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@OtpActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getResedOTP(intent.getStringExtra("mobile_number"))
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        otp = response.body()!!.get("data").asString
                        otp_etOtp.value = ""

//                        Toast.makeText(this@OtpActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(this@OtpActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

//                    Log.d("loginresponse", response.body()!!.asString)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

}
