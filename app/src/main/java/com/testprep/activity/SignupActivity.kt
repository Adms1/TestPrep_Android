package com.testprep.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signup_btnSignup.setOnClickListener {

            if (isValid()) {
                callSignupApi()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun isValid(): Boolean {

        var isvalid = true

        if (TextUtils.isEmpty(signup_etFname.text.toString())) {
            signup_etFname.error = "first name must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etLname.text.toString())) {
            signup_etLname.error = "last name must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etEmail.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(signup_etEmail.text.toString()).matches()) {
            signup_etEmail.error = "Please enter valid Email Address"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etPassword.text.toString())) {
            signup_etPassword.error = "password must not be null"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etCPassword.text.toString())) {
            signup_etCPassword.error = "confirm password must not be null"
            isvalid = false
        }

        if (signup_etPassword.text.toString() != signup_etCPassword.text.toString()) {
            signup_etCPassword.error = "password and confirm password must be same"
            isvalid = false
        }

        if (TextUtils.isEmpty(signup_etMobile.text.toString()) || !android.util.Patterns.PHONE.matcher(signup_etMobile.text.toString()).matches()) {
            signup_etMobile.error = "Please enter valid mobile number"
            isvalid = false
        }

        return isvalid

    }

    fun callSignupApi() {

        val sortDialog = Dialog(this@SignupActivity)//,R.style.PauseDialog);//, R.style.PauseDialog);
        val window = sortDialog.window
        val wlp = window!!.attributes
        sortDialog.window!!.attributes.verticalMargin = 0.10f
        wlp.gravity = Gravity.BOTTOM
        window.attributes = wlp

//        sortDialog.window!!.setBackgroundDrawableResource(R.drawable.filter1_1)

        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sortDialog.setCancelable(true)
//        sortDialog.setContentView(getRoot())
        sortDialog.show()

        val apiService = WebClient.getClient().create(WebInterface::class.java)

//        var call: Call<JsonObject>? = null

//        if(intent.getStringExtra("comefrom").equals("profile", true)){
//            call = apiService.updateProfile(
//                WebRequests.addSignupParams(
//                    Utils.getStringValue(this@SignupActivity, AppConstants.USER_LOGIN_TYPE, "")!!,
//                    Utils.getStringValue(this@SignupActivity, AppConstants.USER_ID, "")!!,
//                    signup_etFname.text.toString(),
//                    signup_etLname.text.toString(),
//                    signup_etEmail.text.toString(),
//                    signup_etPassword.text.toString(),
//                    signup_etMobile.text.toString(),
//                    Utils.getStringValue(this@SignupActivity, AppConstants.USER_STATUSID, "")!!
//                )
//            )
//        }else if(intent.getStringExtra("comefrom").equals("login", true)) {
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

                if (response.body()!!.get("Status").asString == "true") {

                    sortDialog.dismiss()
//                    Toast.makeText(this@SignupActivity, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG).show()

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
                        AppConstants.USER_LOGIN_TYPE,
                        response.body()!!["data"].asJsonArray[0].asJsonObject["LoginTypeID"].asString
                    )
                    Utils.setStringValue(
                        this@SignupActivity,
                        AppConstants.USER_STATUSID,
                        response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                    )

                    val intent = Intent(this@SignupActivity, OtpActivity ::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()

                    Log.d("websize", response.body()!!.get("Msg").asString)

                } else {

                    sortDialog.dismiss()
                    Toast.makeText(this@SignupActivity, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG).show()

                    Log.d("websize", response.body()!!.get("Msg").asString)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                sortDialog.dismiss()
            }
        })

    }

}
