package com.testcraft.testcraft.activity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.AppConstants.Companion.isPrefrence
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import org.json.JSONObject
import org.jsoup.Jsoup
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.io.IOException
import kotlin.system.exitProcess

class SplashActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)

        forceUpdate()

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        changeStatusbarColor(context)

        // In Activity's onCreate() for instance

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        setContentView(R.layout.activity_splash)

        connectivity = Connectivity()

//        forceUpdate()

    }

    fun forceUpdate() {
        val packageManager = this.packageManager
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val currentVersion = packageInfo!!.versionName
        ForceUpdateAsync(currentVersion, this).execute()

    }

    class ForceUpdateAsync(private val currentVersion: String, context: Context) :
        AsyncTask<String?, String?, JSONObject>() {
        private var latestVersion: String? = null
        private val context: Context = context

        override fun onPostExecute(jsonObject: JSONObject) {

            if (latestVersion != null) {
                if (!currentVersion.equals(
                        latestVersion,
                        ignoreCase = true
                    )
                ) { // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
                    if (context is SplashActivity) {
                        if (!(context as Activity).isFinishing) {
                            showForceUpdateDialog()
                        }
                    }
                } else {
                    if (Utils.getStringValue(context, AppConstants.isInstall, "") == "") {

                        Utils.setTokenPref(context, AppConstants.DEFAULT_ACTION_ID, "")

                        Log.d(
                            "default acid splash",
                            Utils.getTokenPref(context, AppConstants.DEFAULT_ACTION_ID, "")!!
                        )
                        CommonWebCalls.callToken(
                            context,
                            "0",
                            AppConstants.DEFAULT_TOKEN_ID,
                            "",
                            ""
                        )

                        Utils.setStringValue(context, AppConstants.isInstall, "true")
                    }

                    Handler().postDelayed(
                        /* Runnable
                         * Showing splash screen with a timer. This will be useful when you
                         * want to show case your app logo / company
                         */
                        {

                            AppConstants.isFirst = 0

                            if (!DialogUtils.isNetworkConnected(context)) {
                                exitProcess(0)

                            } else {
                                if (Utils.getStringValue(context, "is_login", "") == "true") {
                                    // This method will be executed once the timer is over
                                    // Start your app main activity

                                    if (Utils.getStringValue(context, isPrefrence, "") == "1") {

                                        Utils.setStringValue(context, "is_login", "true")

                                        val i = Intent(context, DashboardActivity::class.java)
                                        context.startActivity(i)

                                    } else {

                                        val i = Intent(context, NewActivity::class.java)
                                        context.startActivity(i)

                                    }

                                } else {
                                    val i = Intent(context, IntroActivity::class.java)
                                    context.startActivity(i)

                                }
                            }
                            (context as SplashActivity).finish()

                        }, 3000
                    )
                }
            }

            super.onPostExecute(jsonObject)
        }

        fun showForceUpdateDialog() {

            DialogUtils.createConfirmDialog(context, "New version available",
                "There is newer version of this application available. Click OK to upgrade now?",
                "OK", "Exit",

                DialogInterface.OnClickListener { dialog, which ->

                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
                        )
                    )
                },
                DialogInterface.OnClickListener { dialog, which ->

                    dialog.dismiss()
                    exitProcess(0)

                }).show()

        }

        override fun doInBackground(vararg params: String?): JSONObject {
            try {
                latestVersion =
                    Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.packageName.toString() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText()
                Log.e("latestversion", "---$latestVersion")
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return JSONObject()
        }
    }

//    fun callCheckVerifyAccountApi() {
//
//        if (!DialogUtils.isNetworkConnected(context)) {
//            Utils.ping(context, "Connetion not available")
//        }
//
//        DialogUtils.showDialog(context)
//
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call = apiService.checkVerifyAccount(
//            Utils.getStringValue(context, AppConstants.USER_MOBILE, "")!!
//        )
//
//        call.enqueue(object : Callback<JsonObject> {
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//
//                if (response.body() != null) {
//
//                    DialogUtils.dismissDialog()
//
//                    if (response.body()!!["Status"].asString == "true") {
//
//                        if (response.body()!!["data"].asJsonObject["AccountTypeID"].asInt == 1) {
//
//                            if (response.body()!!["data"].asJsonObject["StatusID"].asInt == 1) {
////                        Toast.makeText(this@LoginActivity, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()
//
//                                if (Utils.getStringValue(
//                                        context,
//                                        "is_login",
//                                        ""
//                                    ) == "true"
//                                ) {
//                                    // This method will be executed once the timer is over
//                                    // Start your app main activity
//                                    Utils.setStringValue(context, "is_login", "true")
//
//                                    val i = Intent(context, NewActivity::class.java)
//                                    startActivity(i)
//
//                                } else {
//                                    val i = Intent(context, IntroActivity::class.java)
//                                    startActivity(i)
//
//                                }
//                            } else if (response.body()!!["data"].asJsonObject["StatusID"].asInt == 2) {
//                                val intent = Intent(context, OtpActivity::class.java)
//                                intent.putExtra(
//                                    "mobile_number",
//                                    Utils.getStringValue(
//                                        context,
//                                        AppConstants.USER_MOBILE,
//                                        ""
//                                    )!!
//                                )
//                                intent.putExtra(
//                                    "otp",
//                                    Utils.getStringValue(
//                                        context,
//                                        AppConstants.OTP,
//                                        ""
//                                    )!!
//                                )
//                                intent.putExtra("come_from", "signup")
//                                startActivity(intent)
//                            }
//                        } else {
//
//                            if (Utils.getStringValue(
//                                    context,
//                                    "is_login",
//                                    ""
//                                ) == "true"
//                            ) {
//                                // This method will be executed once the timer is over
//                                // Start your app main activity
//                                Utils.setStringValue(context, "is_login", "true")
//
//                                val i = Intent(context, NewActivity::class.java)
//                                startActivity(i)
//
//                            } else {
//                                val i = Intent(context, IntroActivity::class.java)
//                                startActivity(i)
//
//                            }
//                        }
//
//                        finish()
//                    }
//
//                } else {
//                    Toast.makeText(
//                        context,
//                        response.body()!!["Msg"].asString,
//                        Toast.LENGTH_LONG
//                    ).show()
//
////                    Log.d("loginresponse", response.body()!!.asString)
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                // Log error here since request failed
//                Log.e("", t.toString())
//                DialogUtils.dismissDialog()
//            }
//        })
//    }
//

}
