package com.testcraft.testcraft.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.appevents.AppEventsLogger
import com.google.gson.JsonObject
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.*
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_payment_success_screen.*
import kotlinx.android.synthetic.main.fragment_market_place_bottom_sheet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentSuccessScreen : AppCompatActivity() {

    var transid = ""
    var pkgname = ""
    var pkgprice = ""
    var comefrom = ""

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    var connectivity: Connectivity? = null

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivity, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivity)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_payment_success_screen)

        connectivity = Connectivity()

        pkgname = intent.getStringExtra("pkgname")
        transid = intent.getStringExtra("transactionId")
        pkgprice = intent.getStringExtra("pkgprice")
        comefrom = intent.getStringExtra("comefrom")

        Utils.setFont(this@PaymentSuccessScreen, "fonts/Inter-SemiBold.ttf", tvTransIdTxt)
        Utils.setFont(this@PaymentSuccessScreen, "fonts/Inter-SemiBold.ttf", tvPkgnameTxt)
        Utils.setFont(this@PaymentSuccessScreen, "fonts/Inter-SemiBold.ttf", tvPriceTxt)

        if (transid != "") {
            tvTransId.text = transid

        } else {
            tvTransId.text = "0"

        }

        tvPkgname.text = pkgname
        tvPrice.text = "₹ $pkgprice"

        if (intent.getStringExtra("responseCode").equals("0", ignoreCase = true)) {

            CommonWebCalls.callToken(
                this@PaymentSuccessScreen,
                "1",
                "",
                ActionIdData.C1500,
                ActionIdData.T1500
            )

//            tvCancel.visibility = GONE

            imvSuccessFail!!.setImageResource(R.drawable.payment_success_icn)
//            imvSuccessFail!!.background = resources.getDrawable(R.drawable.blue_round)
//            Utils.ping(this@PaymentSuccessScreen, "Success")
            tvMessage.text = "Your transaction has been successfully completed"

            tvTry.text = "OK"
            tvTry.background = resources.getDrawable(R.drawable.light_blue_round_bg)

            if (comefrom == "package") {
                updatePaymentStatus("Success")
            } else {
                updatepayment("Success")
            }

        } else {

//            CommonWebCalls.callToken(
//                this@PaymentSuccessScreen,
//                "1",
//                "",
//                ActionIdData.C1400,
//                ActionIdData.T1400
//            )
//
            tvTry.visibility = GONE
//
//            imvSuccessFail!!.setImageResource(R.drawable.payment_fail_icn)
////            imvSuccessFail!!.background = resources.getDrawable(R.drawable.fail_icon)
////            Utils.ping(this@PaymentSuccessScreen, "fail")
//            tvMessage.text = "Your last transaction is fail"
//
//            tvTry.text = "Try Again"
//            tvTry.background = resources.getDrawable(R.drawable.google_round_bg)

//            updatePaymentStatus("Failed")
        }

        tvCancel.setOnClickListener {
            //            val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

            // close this activity
//            finish()

            AppConstants.isFirst = 1

            val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
            startActivity(intent)
            finish()
//            onBackPressed()
        }

        tvTry.setOnClickListener {

            if (tvTry.text == "OK") {

                CommonWebCalls.callToken(
                    this@PaymentSuccessScreen,
                    "1",
                    "",
                    ActionIdData.C1600,
                    ActionIdData.T1600
                )

                AppConstants.isFirst = 1

                val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
                startActivity(intent)
                finish()

            } else {

                CommonWebCalls.callToken(
                    this@PaymentSuccessScreen,
                    "1",
                    "",
                    ActionIdData.C1401,
                    ActionIdData.T1401
                )

                generateTrackNPayRequest(this@PaymentSuccessScreen, intent.getStringExtra("amount"))
            }
        }

    }

    fun updatePaymentStatus(transaction_status: String) {
        if (!DialogUtils.isNetworkConnected(this@PaymentSuccessScreen)) {
            Utils.ping(this@PaymentSuccessScreen, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(this@PaymentSuccessScreen)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.updatePaymentStatus(
            WebRequests.getPaymentStatusParams(
                Utils.getStringValue(this@PaymentSuccessScreen, AppConstants.USER_ID, "")!!,
                intent.getStringExtra("order_id"),
                intent.getStringExtra("transactionId"),
                transaction_status
            )
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString != "true") {

//                        if (transaction_status == "Success") {
//                            Handler().postDelayed(
//
//                                /* Runnable
//                                 * Showing splash screen with a timer. This will be useful when you
//                                 * want to show case your app logo / company
//                                 */
//
//                                {
//                                    // This method will be executed once the timer is over
//                                    // Start your app main activity
////                                    val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
////                                    startActivity(intent)
////                                    overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)
//
//                                    // close this activity
////                                    finish()
//                                    onBackPressed()
//                                }, 1500

//                            )

//                            callAddTestPackageApi()

//                        }

//                    } else {
                        Toast.makeText(
                            this@PaymentSuccessScreen,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_LONG
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

    fun callAddTestPackageApi() {

        if (!DialogUtils.isNetworkConnected(this@PaymentSuccessScreen)) {
            Utils.ping(this@PaymentSuccessScreen, AppConstants.NETWORK_MSG)
        }

//        DialogUtils.showDialog(this@PaymentSuccessScreen)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.addTestPackage(
            Utils.getStringValue(this@PaymentSuccessScreen, AppConstants.USER_ID, "0")!!,
            "1"
        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

//                if (response.body() != null) {

//                    if (response.body()!!["Status"].toString() == "true") {

//                        Toast.makeText(
//                            this@PaymentSuccessScreen,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()

//                    } else {

//                        Toast.makeText(
//                            this@PaymentSuccessScreen,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
                DialogUtils.dismissDialog()
            }
        })
    }

    fun generateTrackNPayRequest(context: Context, coin: String) {
        if (!DialogUtils.isNetworkConnected(context)) {
            Utils.ping(context, AppConstants.NETWORK_MSG)
        }

        DialogUtils.showDialog(context)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

//        if (isBoolean_permission_phoneState) {
//            hashMap["IMEINumber"] = Utils.getIMEI(context)
//        } else {
//            hashMap["IMEINumber"] = ""
//        }
//
//        if (isBoolean_permission_location) {
//            val loc = Utils.getLocation(context)
//            hashMap["Latitude"] = loc[0].toString()
//            hashMap["Longitude"] = loc[1].toString()
//        } else {
//            hashMap["Latitude"] = ""
//            hashMap["Longitude"] = ""
//        }

        val call = apiService.checkout(
            Utils.getStringValue(
                this@PaymentSuccessScreen,
                AppConstants.USER_ID,
                "0"
            )!!, ""
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        Log.v(
                            "order_id: ",
                            "" + response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].toString().replace(
                                "\"",
                                ""
                            )
                        )

                        val intent = Intent(context, TraknpayRequestActivity::class.java)
                        intent.putExtra(
                            "order_id",
                            response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].toString().replace(
                                "\"",
                                ""
                            )
                        )
                        intent.putExtra("amount", coin)
//                        intent.putExtra("transid", transid)
                        intent.putExtra("pkgname", pkgname)
                        intent.putExtra("pkgprice", pkgprice)
                        context.startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            context,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_LONG
                        ).show()

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

    fun updatepayment(transaction_status: String) {
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        DialogUtils.showDialog(this@PaymentSuccessScreen)

        val hashmap: HashMap<String, String> = HashMap()
        hashmap["PaymentOrderID"] = intent.getStringExtra("order_id")!!
        hashmap["StudentID"] =
            Utils.getStringValue(this@PaymentSuccessScreen, AppConstants.USER_ID, "0")!!
        hashmap["ExternalTransactionID"] = intent.getStringExtra("transactionId")
        hashmap["ExternalTransactionStatus"] = transaction_status

        val call = apiService.updatesubscriptionPayment(hashmap)

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()
                if (response.body() != null) {

                    if (response.body()!!["Status"].asString == "true") {

//                        AppConstants.isFirst = 1

//                        val intent =
//                            Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
//                        startActivity(intent)
//                        finish()

//                        MarketPlaceFragment.sheetClose()
//                        Utils.ping(this@PaymentSuccessScreen, "Successfully subscribe")

                        val logger = AppEventsLogger.newLogger(this@PaymentSuccessScreen)
//                        logger.logEvent("Subscription")

                        val params = Bundle()
                        params.putString("Device_Name", "Android")
                        logger.logEvent("Subscription_paid", 1.0, params)

                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                DialogUtils.dismissDialog()
                Log.e("", t.toString())
            }
        })
    }

}
