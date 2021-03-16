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
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.applinks.AppLinkData
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.utils.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.system.exitProcess

class SplashActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    var deepLink: Uri? = null
    var deeplinkcode = ""

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)

//        forceUpdate()

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(this)
//        changeStatusbarColor(context)

        // In Activity's onCreate() for instance

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        setContentView(R.layout.activity_splash)

        connectivity = Connectivity()

        AppLinkData.fetchDeferredAppLinkData(this, getString(R.string.facebook_app_id)) { appLinkData ->

            if (appLinkData != null) {
                deepLink = appLinkData.targetUri
                deeplinkcode = deepLink!!.getQueryParameter("id")!!

                Log.d("SplashActivity", "fblink: $deepLink")

                openfromDplink("fb", deeplinkcode)

            }

        }

//        val intent = intent
//        val action = intent.action
//        val data = intent.data
//        Log.d("SplashActivity", "fblinkdata: $data")

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)

                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    deeplinkcode = deepLink!!.getQueryParameter("id")!!

                    Log.d("dplink", "" + pendingDynamicLinkData.link)

                    openfromDplink("firebase", deeplinkcode)

                }

                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...

                // ...
            }
            .addOnFailureListener(this) { e -> Log.w("dplink", "fail link", e) }

        if (deeplinkcode == "") {
            forceUpdate()
        }

    }

    fun openfromDplink(come: String, dplinkcode: String) {

        if (dplinkcode != "") {

            Utils.setStringValue(
                this@SplashActivity,
                AppConstants.APP_MODE,
                AppConstants.DEEPLINK_MODE
            )

            Utils.setStringValue(this@SplashActivity, AppConstants.IS_DEEPLINK_STEP, "1")
            Utils.setStringValue(this@SplashActivity, AppConstants.DEEPLINK_CODE, dplinkcode)

        } else {

            if (Utils.getStringValue(this@SplashActivity, AppConstants.APP_MODE, "") != AppConstants.DEEPLINK_MODE) {

                if (Utils.getStringValue(this@SplashActivity, AppConstants.APP_MODE, "") == AppConstants.GUEST_MODE) {

                    Utils.setStringValue(this@SplashActivity, AppConstants.APP_MODE, AppConstants.GUEST_MODE)

                } else {
                    Utils.setStringValue(
                        this@SplashActivity,
                        AppConstants.APP_MODE,
                        AppConstants.NORMAL_MODE
                    )
                }
            }

            Log.d("dplink", "no link")

        }

        Log.d("comefromlink", "no link" + come)

        if (deeplinkcode == "") {
            forceUpdate()
        }

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

    class ForceUpdateAsync(private val currentVersion: String, private val context: Context) :
        AsyncTask<String?, String?, JSONObject>() {
        private var latestVersion: String? = null

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

                        val logger = AppEventsLogger.newLogger(context)

                        val params = Bundle()
                        params.putString("Device_Name", "Android")
                        logger.logEvent("Facebook_user_install", 1.0, params)

                        Utils.setStringValue(context, AppConstants.isInstall, "true")

//                        if (Utils.getStringValue(context, AppConstants.APP_MODE, "") != AppConstants.DEEPLINK_MODE) {
//                            Utils.setStringValue(context, AppConstants.APP_MODE, AppConstants.GUEST_MODE)
//                        }

                    } else {

                        CommonWebCalls.callToken(
                            context,
                            "1",
                            "",
                            ActionIdData.C101,
                            ActionIdData.T101
                        )
//                        if (Utils.getStringValue(context, AppConstants.APP_MODE, "") != AppConstants.DEEPLINK_MODE) {
//
//                            if (Utils.getStringValue(context, AppConstants.APP_MODE, "") == AppConstants.GUEST_MODE) {
//
//                                Utils.setStringValue(context, AppConstants.APP_MODE, AppConstants.GUEST_MODE)
//
//                            } else {
//                                Utils.setStringValue(
//                                    context,
//                                    AppConstants.APP_MODE,
//                                    AppConstants.NORMAL_MODE
//                                )
//                            }
//                        }

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

                                if (Utils.getStringValue(
                                        context,
                                        AppConstants.APP_MODE,
                                        ""
                                    ) != AppConstants.DEEPLINK_MODE
                                ) {

                                    if (Utils.getStringValue(
                                            context,
                                            AppConstants.IS_LOGIN,
                                            ""
                                        ) == "true"
                                    ) {
                                        // This method will be executed once the timer is over
                                        // Start your app main activity

//                                        if (Utils.getStringValue(context, isPrefrence, "") == "1") {

                                        Utils.setStringValue(
                                            context,
                                            AppConstants.IS_LOGIN,
                                            "true"
                                        )

                                        val i = Intent(context, DashboardActivity::class.java)
                                        context.startActivity(i)

//                                        } else {

//                                            val i = Intent(context, NewActivity::class.java)
//                                            context.startActivity(i)

//                                        }

                                    } else {
                                        val i = Intent(context, IntroActivity::class.java)
                                        context.startActivity(i)

                                    }

                                } else {

                                    if (Utils.getStringValue(
                                            context,
                                            AppConstants.IS_DEEPLINK_STEP,
                                            ""
                                        ) == "1"
                                    ) {
                                        val i = Intent(context, DeeplinkEntryActivity::class.java)
                                        context.startActivity(i)

                                    } else if (Utils.getStringValue(
                                            context,
                                            AppConstants.IS_DEEPLINK_STEP,
                                            ""
                                        ) == "2" || Utils.getStringValue(
                                            context,
                                            AppConstants.IS_DEEPLINK_STEP,
                                            ""
                                        ) == "3"
                                    ) {

                                        val i = Intent(context, DeeplinkTestActivity::class.java)
                                        context.startActivity(i)
                                    }
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
//                                        AppConstants.IS_LOGIN,
//                                        ""
//                                    ) == "true"
//                                ) {
//                                    // This method will be executed once the timer is over
//                                    // Start your app main activity
//                                    Utils.setStringValue(context, AppConstants.IS_LOGIN, "true")
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
//                                    AppConstants.IS_LOGIN,
//                                    ""
//                                ) == "true"
//                            ) {
//                                // This method will be executed once the timer is over
//                                // Start your app main activity
//                                Utils.setStringValue(context, AppConstants.IS_LOGIN, "true")
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
