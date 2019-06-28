package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class SignupActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_signup)

        signup_btnSignup.setOnClickListener {

            //            val intent = Intent(this@SignupActivity, OtpActivity::class.java)
//            startActivity(intent)
//            finish()

            if (isValid()) {
                callSignupApi()
            }
        }

        signup_btnLogin.setOnClickListener {

            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        signup_ivBack.setOnClickListener {
            onBackPressed()
        }

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

        if (TextUtils.isEmpty(signup_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(signup_etEmail.text.toString()).matches()) {
            signup_etEmail.error = "Please enter valid Email Address"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etPassword.text.toString())) {
            signup_etPassword.error = "Please enter password"
            isvalid = false
        }

//        if (TextUtils.isEmpty(signup_etCPassword.text.toString())) {
//            signup_etCPassword.error = "Please enter confirm password"
//            isvalid = false
//        }

//        if (signup_etPassword.text.toString() != signup_etCPassword.text.toString()) {
//            signup_etCPassword.error = "password and confirm password must be same"
//            isvalid = false
//        }

        if (TextUtils.isEmpty(signup_etMobile.text.toString()) || !android.util.Patterns.PHONE.matcher(signup_etMobile.text.toString()).matches()) {
            signup_etMobile.error = "Please enter valid mobile number"
            isvalid = false
        }

        return isvalid

    }

    fun callSignupApi() {

        if (!DialogUtils.isNetworkConnected(this@SignupActivity)) {
            Utils.ping(this@SignupActivity, "Connetion not available")
        }

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        DialogUtils.showDialog(this@SignupActivity)

        val call = apiService.getSignup(
            WebRequests.addSignupParams(
                "1", "0",
                signup_etFname.text.toString(),
                signup_etLname.text.toString(),
                signup_etEmail.text.toString(),
                signup_etPassword.text.toString(),
                signup_etMobile.text.toString(),
                "1"
            )
        )
//        }

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!.get("Status").asString == "true") {

                        Utils.setStringValue(
                            this@SignupActivity,
                            AppConstants.FIRST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                        )
                        Utils.setStringValue(
                            this@SignupActivity,
                            AppConstants.LAST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                        )
                        Utils.setStringValue(
                            this@SignupActivity,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )
                        Utils.setStringValue(
                            this@SignupActivity,
                            AppConstants.USER_EMAIL,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                        )
                        Utils.setStringValue(
                            this@SignupActivity,
                            AppConstants.USER_PASSWORD,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                        )
                        Utils.setStringValue(
                            this@SignupActivity,
                            AppConstants.USER_MOBILE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                        )
                        Utils.setStringValue(
                            this@SignupActivity,
                            AppConstants.USER_ACCOUNT_TYPE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                        )
                        Utils.setStringValue(
                            this@SignupActivity, AppConstants.USER_STATUSID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                        )

                        val intent = Intent(this@SignupActivity, OtpActivity::class.java)
                        startActivity(intent)
//                        overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)
                        finish()

                        Log.d("websize", response.body()!!.get("Msg").asString)

                    } else {

                        Toast.makeText(this@SignupActivity, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG)
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

    override fun onBackPressed() {

        val intent = Intent(this@SignupActivity, IntroActivity::class.java)
        startActivity(intent)

    }

}
