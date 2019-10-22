package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
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

        setContentView(com.testprep.R.layout.activity_otp)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            otp_tvResend.text = "Did not receive the code? Resend"
        }

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

            if (intent.getStringExtra("come_from") == "forgot password") {

                if (otp_btnSubmit.text.toString() != "Done") {

                    if (otp_etOtp.value.toString() == otp) {

                        otp_tvInvalid.visibility = View.GONE

                        AppConstants.isFirst = 7

                        val intent = Intent(this@OtpActivity, DashboardActivity::class.java)
                        intent.putExtra("come_from", "otp")
                        startActivity(intent)
                        finish()

                    } else {

                        otp_tvInvalid.visibility = View.VISIBLE
                        otp_etOtp.value = ""

                    }
                }
            } else {

                if (otp_btnSubmit.text.toString() != "Done") {

                    if (otp_etOtp.value.toString() == otp) {

                        otp_tvInvalid.visibility = View.GONE

                        Utils.hideKeyboard(this@OtpActivity)
                        otp_tvVerificationSuccess.visibility = View.VISIBLE
                        otp_tvHeading.text = "Awesome!"

                        otp_tvInstruction.visibility = View.GONE
                        otp_btnSubmit.text = "Done"
                        otp_tvResend.visibility = View.GONE
                        otp_etOtp.visibility = View.GONE

                        otp_ivLogo.setImageDrawable(resources.getDrawable(com.testprep.R.drawable.success_verification_icn))

                        if (intent.getStringExtra("come_from") == "signup") {
                            callSignupApi()
                        } else {
                            callupdateApi()
                        }

                    } else {

                        otp_tvInvalid.visibility = View.VISIBLE
                        otp_etOtp.value = ""

                    }

                } else {

                    Utils.setStringValue(this@OtpActivity, "is_login", "true")

                    val intent = Intent(this@OtpActivity, NewActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(otp_etOtp.windowToken, 0)
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

                        Utils.setStringValue(this@OtpActivity, AppConstants.OTP, otp)

//                        Toast.makeText(this@OtpActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(this@OtpActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG)
                            .show()

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

    fun callSignupApi() {

        if (!DialogUtils.isNetworkConnected(this@OtpActivity)) {
            Utils.ping(this@OtpActivity, "Connetion not available")
        }

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        DialogUtils.showDialog(this@OtpActivity)

        val call = apiService.getSignup(
            WebRequests.addSignupParams(
                "1", "0",
                intent.getStringExtra("first_name"),
                intent.getStringExtra("last_name"),
                intent.getStringExtra("email"),
                intent.getStringExtra("password"),
                intent.getStringExtra("mobile_number"),
                intent.getStringExtra("account_type")
            )
        )
//        }

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.FIRST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.LAST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_EMAIL,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_PASSWORD,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_MOBILE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_ACCOUNT_TYPE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity, AppConstants.USER_STATUSID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity, AppConstants.OTP,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["OTP"].asString
                        )

//                        Utils.hideKeyboard(this@OtpActivity)
//                        otp_tvVerificationSuccess.visibility = View.VISIBLE
//                        otp_tvHeading.text = "Awesome!"
//
//                        otp_tvInstruction.visibility = View.GONE
//                        otp_btnSubmit.text = "Done"
//                        otp_tvResend.visibility = View.GONE
//                        otp_etOtp.visibility = View.GONE
//
//                        otp_ivLogo.setImageDrawable(resources.getDrawable(com.testprep.R.drawable.success_verification_icn))

                        Log.d("websize", response.body()!!.get("Msg").asString)

                    } else {

                        Toast.makeText(this@OtpActivity, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG)
                            .show()

                        Log.d("websize", response.body()!!.get("Msg").asString)
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

    fun callupdateApi() {

        if (!DialogUtils.isNetworkConnected(this@OtpActivity)) {
            Utils.ping(this@OtpActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@OtpActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)
        val call = apiService.updateProfile(
            WebRequests.addSignupParams(
                intent.getStringExtra("account_type"),
                Utils.getStringValue(this@OtpActivity, AppConstants.USER_ID, "")!!,
                intent.getStringExtra("first_name"),
                intent.getStringExtra("last_name"),
                intent.getStringExtra("email"),
                intent.getStringExtra("password"),
                intent.getStringExtra("mobile_number"),
                Utils.getStringValue(this@OtpActivity, AppConstants.USER_STATUSID, "")!!
            )
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.FIRST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.LAST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_EMAIL,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_PASSWORD,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_MOBILE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity,
                            AppConstants.USER_ACCOUNT_TYPE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity, AppConstants.USER_STATUSID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                        )
                        Utils.setStringValue(
                            this@OtpActivity, AppConstants.OTP,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["OTP"].asString
                        )

//                        Utils.hideKeyboard(this@OtpActivity)
//                        otp_tvVerificationSuccess.visibility = View.VISIBLE
//                        otp_tvHeading.text = "Awesome!"
//
//                        otp_tvInstruction.visibility = View.GONE
//                        otp_btnSubmit.text = "Done"
//                        otp_tvResend.visibility = View.GONE
//                        otp_etOtp.visibility = View.GONE
//
//                        otp_ivLogo.setImageDrawable(resources.getDrawable(com.testprep.R.drawable.success_verification_icn))

                        Log.d("websize", response.body()!!.get("Msg").asString)

                    } else {

                        Toast.makeText(
                            this@OtpActivity,
                            response.body()!!.get("Msg").asString,
                            Toast.LENGTH_LONG
                        ).show()

                        Log.d("websize", response.body()!!.get("Msg").asString)
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
