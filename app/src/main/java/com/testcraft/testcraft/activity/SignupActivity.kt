package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

//    var connectivity: Connectivity? = null
//
//    override fun onResume() {
//        super.onResume()
//        val filter = IntentFilter()
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
//        registerReceiver(connectivity, filter)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        unregisterReceiver(connectivity)
//    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_signup)

//        connectivity = Connectivity()

        CommonWebCalls.callToken(this@SignupActivity, "1", "", ActionIdData.C300, ActionIdData.T300)

        signup_btnSignup.setOnClickListener {

            CommonWebCalls.callToken(
                this@SignupActivity,
                "1",
                "",
                ActionIdData.C301,
                ActionIdData.T301
            )

            if (isValid()) {

                if (signup_cbTerms.isChecked) {
                    callVerifyAccountApi()
                } else {
                    Utils.ping(this@SignupActivity, "Select Terms & Conditions")
                }
            }
        }

        signup_btnLogin.setOnClickListener {

            CommonWebCalls.callToken(
                this@SignupActivity,
                "1",
                "",
                ActionIdData.C302,
                ActionIdData.T302
            )

            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        signup_tvTerms.setOnClickListener {

            if (!DialogUtils.isNetworkConnected(this@SignupActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
                DialogUtils.NetworkDialog(this@SignupActivity)
                DialogUtils.dismissDialog()
            } else {

                val intent = Intent(this@SignupActivity, ViewInvoiceActivity::class.java)
                intent.putExtra("header", "Terms & Conditions")
                intent.putExtra(
                    "url",
                    "http://testcraft.in/TCTerms.aspx"
                )
                startActivity(intent)
            }
        }

        signup_etMobile.setOnEditorActionListener { v, actionId, event ->

            if (event != null && event.keyCode == KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {

                if (isValid()) {
                    if (signup_cbTerms.isChecked) {
                        callVerifyAccountApi()
                    } else {
                        Utils.ping(this@SignupActivity, "Select Terms & Conditions")
                    }
                }

            }
            false
        }

//        signup_ivBack.setOnClickListener {
//            onBackPressed()
//        }
//

    }

    fun isValid(): Boolean {

        var isvalid = true

        if (TextUtils.isEmpty(signup_etFname.text.toString())) {
            signup_etFname.error = "Please enter first name"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etLname.text.toString())) {
            signup_etLname.error = "Please enter last name"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(
                signup_etEmail.text.toString()
            ).matches()
        ) {
            signup_etEmail.error = "Please enter valid Email Address"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etPassword.text.toString())) {
            signup_etPassword.error = "Please enter password"
            isvalid = false
        }

        if (signup_etPassword.text!!.length < 4) {
            signup_etPassword.error = " you have to enter at least 4 digit!"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etMobile.text.toString()) || !Patterns.PHONE.matcher(
                signup_etMobile.text.toString()
            ).matches() || signup_etMobile.length() < 10
        ) {
            signup_etMobile.error = "Please enter valid mobile number"
            isvalid = false
        }

        return isvalid

    }

    override fun onBackPressed() {

        val intent = Intent(this@SignupActivity, IntroActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun callVerifyAccountApi() {

        if (!DialogUtils.isNetworkConnected(this@SignupActivity)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
            DialogUtils.NetworkDialog(this@SignupActivity)
            DialogUtils.dismissDialog()
        }

        DialogUtils.showDialog(this@SignupActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.verifyAccount(
            signup_etMobile.text.toString(), signup_etEmail.text.toString()
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        Utils.setStringValue(
                            this@SignupActivity,
                            AppConstants.OTP,
                            response.body()!!.get("data").asString
                        )

                        val intent = Intent(this@SignupActivity, OtpActivity::class.java)
                        intent.putExtra("mobile_number", signup_etMobile.text.toString())
                        intent.putExtra("otp", response.body()!!["data"].asString)
                        intent.putExtra("come_from", "signup")
                        intent.putExtra("first_name", signup_etFname.text.toString())
                        intent.putExtra("last_name", signup_etLname.text.toString())
                        intent.putExtra("email", signup_etEmail.text.toString())
                        intent.putExtra("password", signup_etPassword.text.toString())
                        intent.putExtra("account_type", "1")
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_LONG
                        ).show()

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
