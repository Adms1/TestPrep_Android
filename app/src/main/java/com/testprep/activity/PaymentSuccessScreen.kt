package com.testprep.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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

        if (intent.getStringExtra("responseCode").equals("0", ignoreCase = true)) {

            tvTry.visibility = GONE
            tvCancel.visibility = GONE

            imvSuccessFail!!.setImageResource(R.drawable.success_icon)
//            Utils.ping(this@PaymentSuccessScreen, "Success")
            tvMessage.text = "Transaction Successful"
            updatePaymentStatus("Success")

        } else {

            tvTry.visibility = VISIBLE
            tvCancel.visibility = VISIBLE

            imvSuccessFail!!.setImageResource(R.drawable.fail_icon)
//            Utils.ping(this@PaymentSuccessScreen, "fail")
            tvMessage.text = "Transaction Fail"
            updatePaymentStatus("Failed")

        }

        tvCancel.setOnClickListener {
            val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
            startActivity(intent)
//            overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

            // close this activity
            finish()
        }

        tvTry.setOnClickListener {
            generateTrackNPayRequest(this@PaymentSuccessScreen, intent.getStringExtra("amount"))
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
                            Handler().postDelayed(

                                /* Runnable
                                 * Showing splash screen with a timer. This will be useful when you
                                 * want to show case your app logo / company
                                 */

                                {
                                    // This method will be executed once the timer is over
                                    // Start your app main activity
                                    val intent = Intent(this@PaymentSuccessScreen, DashboardActivity::class.java)
                                    startActivity(intent)
//                                    overridePendingTransition(R.anim.slide_in_leftt, R.anim.slide_out_right)

                                    // close this activity
                                    finish()
                                }, 1500
                            )
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

        val call = apiService.getPayment(
            WebRequests.getPaymentParams(
                "0", Utils.getStringValue(
                    context,
                    AppConstants.USER_ID,
                    ""
                )!!, coin
            )
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
                        context.startActivity(intent)

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
