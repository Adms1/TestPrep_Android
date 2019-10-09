package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
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

        setContentView(com.testprep.R.layout.activity_signup)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            signup_btnLogin.text = "ALREADY HAVE AN ACCOUNT?SIGN IN"
        }

        signup_btnSignup.setOnClickListener {

            //            val intent = Intent(this@SignupActivity, OtpActivity::class.java)
//            startActivity(intent)
//            finish()

            if (isValid()) {
                callVerifyAccountApi()
            }
        }

        signup_btnLogin.setOnClickListener {

            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        signup_etMobile.setOnEditorActionListener(object : OnEditorActionListener {

            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (event != null && event.keyCode == KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    if (isValid()) {
                        callVerifyAccountApi()
                    }
                }
                return false
            }
        })
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

        if (signup_etPassword.text!!.length < 4) {
            signup_etPassword.error = " you have to enter at least 4 digit!"
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

        if (TextUtils.isEmpty(signup_etMobile.text.toString()) || !Patterns.PHONE.matcher(signup_etMobile.text.toString()).matches() || signup_etMobile.length() < 10) {
            signup_etMobile.error = "Please enter valid mobile number"
            isvalid = false
        }

        return isvalid

    }

    override fun onBackPressed() {

        val intent = Intent(this@SignupActivity, IntroActivity::class.java)
        startActivity(intent)

    }

    fun callVerifyAccountApi() {

        if (!DialogUtils.isNetworkConnected(this@SignupActivity)) {
            Utils.ping(this@SignupActivity, "Connetion not available")
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
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@SignupActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

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
