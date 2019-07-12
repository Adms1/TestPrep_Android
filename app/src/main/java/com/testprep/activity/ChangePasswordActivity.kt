package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ChangePasswordActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_change_password)

        if (intent.getStringExtra("come_from") == "other") {

            chngepass_ivoldpass.visibility = View.VISIBLE
            chngepass_llOldpass.visibility = View.VISIBLE

        } else {

            chngepass_ivoldpass.visibility = View.GONE
            chngepass_llOldpass.visibility = View.GONE

        }

        chngepass_btnChange.setOnClickListener {

            if (intent.getStringExtra("come_from") == "other") {
                if (chngepass_etOldpass.text!!.length < 4) {
                    chngepass_etOldpass.error = " you have to enter at least 4 digit!"
                }
            }

            if (chngepass_etNewpass.text!!.length < 4) {
                chngepass_etNewpass.error = " you have to enter at least 4 digit!"
            } else if (chngepass_etconfirmpass.text!!.length < 4) {
                chngepass_etconfirmpass.error = " you have to enter at least 4 digit!"
            } else {
                if (chngepass_etNewpass.text.toString() == chngepass_etconfirmpass.text.toString()) {
                    callChangePasswordlApi()
                } else {
                    chngepass_etconfirmpass.error = "Password not match"
                }
            }
        }

        chngepass_ivBack.setOnClickListener {

            onBackPressed()
        }

    }

    fun callChangePasswordlApi() {

        if (!DialogUtils.isNetworkConnected(this@ChangePasswordActivity)) {
            Utils.ping(this@ChangePasswordActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@ChangePasswordActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.changePassword(
            WebRequests.changePasswordParams(
                Utils.getStringValue(this@ChangePasswordActivity, AppConstants.USER_ID, "")!!,
                chngepass_etNewpass.text.toString()
            )
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        Toast.makeText(
                            this@ChangePasswordActivity,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        Utils.setStringValue(this@ChangePasswordActivity, "is_login", "true")

//                        overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

                        Utils.setStringValue(
                            this@ChangePasswordActivity,
                            AppConstants.FIRST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                        )
                        Utils.setStringValue(
                            this@ChangePasswordActivity,
                            AppConstants.LAST_NAME,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                        )
                        Utils.setStringValue(
                            this@ChangePasswordActivity,
                            AppConstants.USER_ID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                        )
                        Utils.setStringValue(
                            this@ChangePasswordActivity,
                            AppConstants.USER_EMAIL,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                        )
                        Utils.setStringValue(
                            this@ChangePasswordActivity,
                            AppConstants.USER_PASSWORD,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                        )
                        Utils.setStringValue(
                            this@ChangePasswordActivity,
                            AppConstants.USER_MOBILE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                        )
                        Utils.setStringValue(
                            this@ChangePasswordActivity,
                            AppConstants.USER_ACCOUNT_TYPE,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                        )
                        Utils.setStringValue(
                            this@ChangePasswordActivity,
                            AppConstants.USER_STATUSID,
                            response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                        )

                        Handler().postDelayed(
                            /* Runnable
                                 * Showing splash screen with a timer. This will be useful when you
                                 * want to show case your app logo / company
                                 */

                            {
                                val intent = Intent(this@ChangePasswordActivity, NewActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 2500
                        )

                    } else {
                        Toast.makeText(
                            this@ChangePasswordActivity,
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
