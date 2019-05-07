package com.testprep.activity

import android.app.Dialog
import android.content.Context
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
import com.testprep.old.retrofit.WebClient
import com.testprep.old.retrofit.WebInterface
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

            //            val intent = Intent(this@SignupActivity, OtpActivity::class.java)
//            startActivity(intent)
//           overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            if (isValid()) {
                callSignupApi()
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

        val call = apiService.getSignup(WebRequests.addSignupParams("0",
            signup_etFname.text.toString(),
            signup_etLname.text.toString(),
            signup_etEmail.text.toString(),
            signup_etPassword.text.toString(),
            signup_etMobile.text.toString(),
            "1"))
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!.get("Status").asString == "true") {

                    sortDialog.dismiss()
                    Toast.makeText(this@SignupActivity, response.body()!!.get("Msg").asString, Toast.LENGTH_LONG).show()

                    val intent = Intent(this@SignupActivity, OtpActivity ::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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
