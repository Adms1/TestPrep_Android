package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.JsonObject
import com.testprep.R
import com.testprep.retrofit.WebClient
import com.testprep.retrofit.WebInterface
import com.testprep.utils.AppConstants
import com.testprep.utils.DialogUtils
import com.testprep.utils.Utils
import com.testprep.utils.WebRequests
import kotlinx.android.synthetic.main.activity_payment_success_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class PaymentSuccessScreen : AppCompatActivity() {

    var pkgid = ""
    var pkgname = ""
    var pkgprice = ""

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_payment_success_screen)

        pkgid = intent.getStringExtra("pkgid")
        pkgname = intent.getStringExtra("transactionId")
        pkgprice = intent.getStringExtra("pkgprice")

        if (pkgname != "") {
            tvPkgname.text = "Transaction Id : " + pkgname

        } else {
            tvPkgname.text = "Transaction Id : 0"

        }
        tvPrice.text = "Price                : " + pkgprice

        if (intent.getStringExtra("responseCode").equals("0", ignoreCase = true)) {

            tvCancel.visibility = GONE

            imvSuccessFail!!.setImageResource(R.drawable.payment_success_icn)
//            imvSuccessFail!!.background = resources.getDrawable(R.drawable.blue_round)
//            Utils.ping(this@PaymentSuccessScreen, "Success")
            tvMessage.text = "Your transaction has been successfully completed"

            tvTry.text = "OK"
            tvTry.background = resources.getDrawable(R.drawable.light_blue_round_bg)

            updatePaymentStatus("Success")

        } else {

            tvCancel.visibility = VISIBLE

            imvSuccessFail!!.setImageResource(R.drawable.payment_fail_icn)
//            imvSuccessFail!!.background = resources.getDrawable(R.drawable.fail_icon)
//            Utils.ping(this@PaymentSuccessScreen, "fail")
            tvMessage.text = "Your last transaction is fail"

            tvTry.text = "Try Again"
            tvTry.background = resources.getDrawable(R.drawable.google_round_bg)

//            updatePaymentStatus("Failed")
        }

        tvCancel.setOnClickListener {
            //            val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

            // close this activity
//            finish()
            onBackPressed()
        }

        tvTry.setOnClickListener {

            if (tvTry.text == "OK") {

                AppConstants.isFirst = 1

                val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
                startActivity(intent)
                finish()

            } else {

                generateTrackNPayRequest(this@PaymentSuccessScreen, intent.getStringExtra("amount"))
            }
        }

    }

    fun updatePaymentStatus(transaction_status: String) {
        if (!DialogUtils.isNetworkConnected(this@PaymentSuccessScreen)) {
            Utils.ping(this@PaymentSuccessScreen, "Connetion not available")
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

                    if (response.body()!!["Status"].asString == "true") {

                        if (transaction_status == "Success") {
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


                        }

                    } else {
                        Toast.makeText(this@PaymentSuccessScreen, response.body()!!["Msg"].asString, Toast.LENGTH_LONG)
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
            Utils.ping(this@PaymentSuccessScreen, "Connetion not available")
        }

//        DialogUtils.showDialog(this@PaymentSuccessScreen)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.addTestPackage(
            Utils.getStringValue(this@PaymentSuccessScreen, AppConstants.USER_ID, "0")!!,
            intent.getStringExtra("pkgid")
        )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    if (response.body()!!["Status"].toString() == "true") {

//                        Toast.makeText(
//                            this@PaymentSuccessScreen,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()

                    } else {

//                        Toast.makeText(
//                            this@PaymentSuccessScreen,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
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


    fun generateTrackNPayRequest(context: Context, coin: String) {
        if (!DialogUtils.isNetworkConnected(context)) {
            Utils.ping(context, "Connetion not available")
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

        val call = apiService.checkout(Utils.getStringValue(this@PaymentSuccessScreen, AppConstants.USER_ID, "0")!!)

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
                        intent.putExtra("pkgid", pkgid)
                        intent.putExtra("pkgname", pkgname)
                        intent.putExtra("pkgprice", pkgprice)
                        context.startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(context, response.body()!!["Msg"].asString, Toast.LENGTH_LONG).show()

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
