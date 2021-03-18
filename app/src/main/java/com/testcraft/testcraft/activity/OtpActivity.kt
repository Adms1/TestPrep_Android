package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.JsonObject
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.utils.*
import com.testcraft.testcraft.utils.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

class OtpActivity : AppCompatActivity() {

    var otp = ""
    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: SmsBroadcastReceiver? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
        unregisterReceiver(smsBroadcastReceiver)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_otp)

        connectivity = Connectivity()

        CommonWebCalls.callToken(this@OtpActivity, "1", "", ActionIdData.C400, ActionIdData.T400)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            otp_tvResend.text = "Did not receive the code? Resend"
        }

        otp = intent.getStringExtra("otp")

        otp_ivBack.setOnClickListener { onBackPressed() }

        otp_tvInstruction.text =
            "Please enter verification code \n sent to +91 " + intent.getStringExtra("mobile_number")

        otp_tvResend.setOnClickListener {

            otp_tvResend.isClickable = false
            otp_tvResend.postDelayed(Runnable { otp_tvResend.isClickable = true }, 2000)

            CommonWebCalls.callToken(
                this@OtpActivity,
                "1",
                "",
                ActionIdData.C401,
                ActionIdData.T401
            )

            if (otp_etOtp.value.toString() != "") {
                otp_etOtp.value = ""
            }

            Toast.makeText(
                this@OtpActivity,
                "OTP has been sent to your registered mobile number",
                Toast.LENGTH_LONG
            )
                .show()
            callResend()

        }

        otp_btnSubmit.setOnClickListener {

            otp_btnSubmit.isClickable = false
            otp_btnSubmit.postDelayed(Runnable { otp_btnSubmit.isClickable = true }, 2000)

            if (intent.getStringExtra("come_from") == "forgot password") {

                if (otp_btnSubmit.text.toString() != "Done") {

                    CommonWebCalls.callToken(
                        this@OtpActivity,
                        "1",
                        "",
                        ActionIdData.C4002,
                        ActionIdData.T4002
                    )

                    if (otp_etOtp.value.toString() == otp) {

                        otp_tvInvalid.visibility = View.GONE

                        AppConstants.isFirst = 7

                        val intent = Intent(this@OtpActivity, DashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
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

                    CommonWebCalls.callToken(
                        this@OtpActivity,
                        "1",
                        "",
                        ActionIdData.C402,
                        ActionIdData.T402
                    )

                    if (otp_etOtp.value.toString() == otp) {

                        CommonWebCalls.callToken(
                            this@OtpActivity,
                            "1",
                            "",
                            ActionIdData.C500,
                            ActionIdData.T500
                        )

                        otp_tvInvalid.visibility = View.GONE

                        Utils.hideKeyboard(this@OtpActivity)
//                        otp_tvVerificationSuccess.visibility = View.VISIBLE
//                        otp_tvHeading.text = "Awesome!"
//
//                        otp_tvInstruction.visibility = View.GONE
//                        otp_btnSubmit.text = "Done"
//                        otp_tvResend.visibility = View.GONE
//                        otp_etOtp.visibility = View.GONE

//                        otp_ivLogo.setImageDrawable(resources.getDrawable(R.drawable.success_verification_icn))

                        if (intent.getStringExtra("come_from") == "signup") {

                            otp_tvVerificationSuccess.visibility = View.VISIBLE
                            otp_tvHeading.text = "Awesome!"

                            otp_tvInstruction.visibility = View.GONE
                            otp_btnSubmit.text = "Done"
                            otp_tvResend.visibility = View.GONE
                            otp_etOtp.visibility = View.GONE

                            otp_ivLogo.setImageDrawable(resources.getDrawable(R.drawable.success_verification_icn))

                            if (intent.getStringExtra("student_id") == "0") {

                                CommonWebCalls.callSignupApi("otp", this@OtpActivity,
                                    "5",
                                    "0",
                                    "",
                                    "",
                                    intent.getStringExtra("email"),
                                    intent.getStringExtra("password"),
                                    intent.getStringExtra("mobile_number"))
                            } else {

                                if (Utils.getStringValue(this@OtpActivity, AppConstants.isPrefrence, "") == "1") {

                                    Utils.setStringValue(
                                        this@OtpActivity,
                                        AppConstants.IS_LOGIN,
                                        "true"
                                    )

                                    val i = Intent(this@OtpActivity, DashboardActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(i)
                                    finish()
                                } else {
                                    val i = Intent(this@OtpActivity, NewActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(i)
                                    finish()
                                }
                            }

                        } else {
                            callupdateApi()
                        }

                    } else {

                        otp_tvInvalid.visibility = View.VISIBLE
                        otp_etOtp.value = ""

                    }

                } else {

                    CommonWebCalls.callToken(
                        this@OtpActivity,
                        "1",
                        "",
                        ActionIdData.C501,
                        ActionIdData.T501
                    )

                    Utils.setStringValue(this@OtpActivity, AppConstants.IS_LOGIN, "true")

                    if (Utils.getStringValue(this@OtpActivity, AppConstants.isPrefrence, "") == "1") {

                        AppConstants.isFirst = 0

                        val i = Intent(this@OtpActivity, DashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                        finish()

                    } else {
                        val i = Intent(this@OtpActivity, NewActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                        finish()
                    }
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
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(this@OtpActivity)
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(this@OtpActivity)

        val call = WebClient.buildService().getResedOTP(intent.getStringExtra("mobile_number"))
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

//                otp_tvResend.isClickable = true

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        otp = response.body()!!.get("data").asString

                        Utils.setStringValue(this@OtpActivity, AppConstants.OTP, otp)

                        startSmsUserConsent()
//                        Toast.makeText(this@OtpActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(
                            this@OtpActivity,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_LONG
                        )
                            .show()

//                    Log.d("loginresponse", response.body()!!.asString)
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

//                otp_tvResend.isClickable = true
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun callSignupApi() {

        if (!DialogUtils.isNetworkConnected(this@OtpActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(this@OtpActivity)
            DialogUtils.dismissDialog()
        }



        DialogUtils.showDialog(this@OtpActivity)

        val call = WebClient.buildService().getSignup(
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

                        Toast.makeText(
                            this@OtpActivity,
                            response.body()!!.get("Msg").asString,
                            Toast.LENGTH_LONG
                        )
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
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(this@OtpActivity)
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(this@OtpActivity)

        val call = WebClient.buildService().updateProfile(
            WebRequests.addSignupParams(
                intent.getStringExtra("account_type"),
                Utils.getStringValue(this@OtpActivity, AppConstants.USER_ID, "")!!,
                intent.getStringExtra("first_name"),
                intent.getStringExtra("last_name"),
                intent.getStringExtra("email"),
                intent.getStringExtra("password"),
                intent.getStringExtra("mobile_number"),
                Utils.getStringValue(this@OtpActivity, AppConstants.USER_STATUSID, "")!!)
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

    override fun onBackPressed() {

        val intent = Intent(this@OtpActivity, PhoneLoginActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null)
            .addOnSuccessListener {
//                Toast.makeText(applicationContext, "On Success", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
//                Toast.makeText(applicationContext, "On OnFailure", Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
//                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                otp_etOtp.value = String.format("%s - %s", getString(R.string.received_message), message)
                getOtpFromMessage(message)
            }
        }
    }

    private fun getOtpFromMessage(message: String) {
        // This will match any 6 digit number in the message
        val pattern: Pattern = Pattern.compile("(|^)\\d{6}")
        val matcher: Matcher = pattern.matcher(message)
        if (matcher.find()) {
            otp_etOtp.value = matcher.group(0)
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    startActivityForResult(intent, REQ_USER_CONSENT)
                }

                override fun onFailure() {}
            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)
        registerBroadcastReceiver()
    }

}
