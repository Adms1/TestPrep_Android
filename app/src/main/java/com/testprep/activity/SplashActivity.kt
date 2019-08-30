package com.testprep.activity

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        changeStatusbarColor(this@SplashActivity)

        // In Activity's onCreate() for instance

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            /* Runnable
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

            {

                if (Utils.getStringValue(this@SplashActivity, "is_login", "") == "true") {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Utils.setStringValue(this@SplashActivity, "is_login", "true")

                    val i = Intent(this@SplashActivity, NewActivity::class.java)
                    startActivity(i)

                } else {
                    val i = Intent(this@SplashActivity, IntroActivity::class.java)
                    startActivity(i)

                }

            }, 3000
        )

    }

    fun callCheckVerifyAccountApi() {

        if (!DialogUtils.isNetworkConnected(this@SplashActivity)) {
            Utils.ping(this@SplashActivity, "Connetion not available")
        }

        DialogUtils.showDialog(this@SplashActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.checkVerifyAccount(
            Utils.getStringValue(this@SplashActivity, AppConstants.USER_MOBILE, "")!!
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        if (response.body()!!["data"].asJsonObject["AccountTypeID"].asInt == 1) {

                            if (response.body()!!["data"].asJsonObject["StatusID"].asInt == 1) {
//                        Toast.makeText(this@LoginActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

                                if (Utils.getStringValue(this@SplashActivity, "is_login", "") == "true") {
                                    // This method will be executed once the timer is over
                                    // Start your app main activity
                                    Utils.setStringValue(this@SplashActivity, "is_login", "true")

                                    val i = Intent(this@SplashActivity, NewActivity::class.java)
                                    startActivity(i)

                                } else {
                                    val i = Intent(this@SplashActivity, IntroActivity::class.java)
                                    startActivity(i)

                                }
                            } else if (response.body()!!["data"].asJsonObject["StatusID"].asInt == 2) {
                                val intent = Intent(this@SplashActivity, OtpActivity::class.java)
                                intent.putExtra(
                                    "mobile_number",
                                    Utils.getStringValue(this@SplashActivity, AppConstants.USER_MOBILE, "")!!
                                )
                                intent.putExtra(
                                    "otp",
                                    Utils.getStringValue(this@SplashActivity, AppConstants.OTP, "")!!
                                )
                                intent.putExtra("come_from", "signup")
                                startActivity(intent)
                            }
                        } else {

                            if (Utils.getStringValue(this@SplashActivity, "is_login", "") == "true") {
                                // This method will be executed once the timer is over
                                // Start your app main activity
                                Utils.setStringValue(this@SplashActivity, "is_login", "true")

                                val i = Intent(this@SplashActivity, NewActivity::class.java)
                                startActivity(i)

                            } else {
                                val i = Intent(this@SplashActivity, IntroActivity::class.java)
                                startActivity(i)

                            }
                        }

                        finish()
                    }

                } else {
                    Toast.makeText(this@SplashActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

//                    Log.d("loginresponse", response.body()!!.asString)
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
