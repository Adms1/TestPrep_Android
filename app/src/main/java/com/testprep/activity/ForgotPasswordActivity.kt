package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ForgotPasswordActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_forgot_password)

        forgot_pass_btnReset.setOnClickListener {

            if (TextUtils.isEmpty(forgot_pass_etEmail.text.toString())) {
                forgot_pass_etEmail.error = "Please Enter Email"
            } else {
//                callForgotPasswordlApi()

                val intent = Intent(this@ForgotPasswordActivity, CheckEmailActivity::class.java)
                startActivity(intent)
                finish()

            }
        }

        forgot_pass_ivBack.setOnClickListener { onBackPressed() }

    }

    fun callForgotPasswordlApi() {

        if (!DialogUtils.isNetworkConnected(this@ForgotPasswordActivity)) {
            Utils.ping(this@ForgotPasswordActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@ForgotPasswordActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.forgotPassword(WebRequests.checkEmailParams(forgot_pass_etEmail.text.toString()))
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_LONG
                        )
                            .show()

                        val intent = Intent(this@ForgotPasswordActivity, CheckEmailActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_SHORT
                        )
                            .show()
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
