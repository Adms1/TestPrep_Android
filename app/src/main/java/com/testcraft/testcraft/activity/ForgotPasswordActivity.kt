package com.testcraft.testcraft.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import com.testcraft.testcraft.utils.WebRequests
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

            when {
                TextUtils.isEmpty(forgot_pass_etEmail.text.toString()) -> forgot_pass_etEmail.error =
                    "Please Enter Mobile Number"
                forgot_pass_etEmail.text!!.length != 10 -> forgot_pass_etEmail.error =
                    "Please enter valid Mobile Number"
                else -> callForgotPasswordlApi()

                //                val intent = Intent(this@ForgotPasswordActivity, CheckEmailActivity::class.java)
                //                startActivity(intent)
                //                finish()
            }
        }

        forgot_pass_etEmail.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                    when {
                        TextUtils.isEmpty(forgot_pass_etEmail.text.toString()) -> forgot_pass_etEmail.error =
                            "Please Enter Mobile Number"
                        forgot_pass_etEmail.text!!.length != 10 -> forgot_pass_etEmail.error =
                            "Please enter valid Mobile Number"
                        else -> callForgotPasswordlApi()

                        //                val intent = Intent(this@ForgotPasswordActivity, CheckEmailActivity::class.java)
                        //                startActivity(intent)
                        //                finish()
                    }
                }
                return false
            }
        })


        forgot_pass_ivBack.setOnClickListener { onBackPressed() }

    }

    fun callForgotPasswordlApi() {

        if (!DialogUtils.isNetworkConnected(this@ForgotPasswordActivity)) {
            Utils.ping(this@ForgotPasswordActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@ForgotPasswordActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call =
            apiService.forgotPassword(WebRequests.checkForgotpassParams(forgot_pass_etEmail.text.toString()))
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

//                        Toast.makeText(
//                            this@ForgotPasswordActivity,
//                            response.body()!!["Msg"].asString,
//                            Toast.LENGTH_LONG
//                        )
//                            .show()

                        Utils.setStringValue(
                            this@ForgotPasswordActivity,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )

                        val intent = Intent(this@ForgotPasswordActivity, OtpActivity::class.java)
                        intent.putExtra("mobile_number", forgot_pass_etEmail.text.toString())
                        intent.putExtra(
                            "otp",
                            response.body()!!["data"].asJsonArray[0].asJsonObject["OTP"].asString
                        )
                        intent.putExtra("come_from", "forgot password")
                        intent.putExtra("first_name", "")
                        intent.putExtra("last_name", "")
                        intent.putExtra("email", "")
                        intent.putExtra("password", "")
                        intent.putExtra("account_type", "1")
                        startActivity(intent)

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
