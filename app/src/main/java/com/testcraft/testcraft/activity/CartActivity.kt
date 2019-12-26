package com.testcraft.testcraft.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import com.testcraft.testcraft.utils.WebRequests
import kotlinx.android.synthetic.main.activity_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class CartActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_cart)

        cart_ivBack.setOnClickListener { onBackPressed() }

        cart_card_view.setOnClickListener {
            val intent = Intent(this@CartActivity, ApplyCouponActivity::class.java)
            startActivity(intent)

        }

        getCart()

        cart_btnPay.setOnClickListener {
            callCheckout()
        }

    }

    private fun getCart() {
        if (!DialogUtils.isNetworkConnected(this@CartActivity)) {
            Utils.ping(this@CartActivity, "Connection not available")
        }

        DialogUtils.showDialog(this@CartActivity)
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call =
            apiService.getCart(Utils.getStringValue(this@CartActivity, AppConstants.USER_ID, "0")!!)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                DialogUtils.dismissDialog()

                if (response.body() != null) {

                    if (response.body()!!["Status"].asString == "true") {

                        cart_tvPname.text =
                            response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TestPackageName")
                                .asString

                        if (response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TestPackageName").asString != "") {
                            cart_name_short.text =
                                response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TestPackageName")
                                    .asString.substring(0, 1)
                        }

                        cart_tvPprice.text =
                            "₹" + response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TestPackageSalePrice").asString
                        cart_tvPayable.text =
                            "₹" + response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TestPackageSalePrice").asString
                        cart_tvTotal.text =
                            "₹" + response.body()!!.get("data").asJsonArray[0].asJsonObject.get("TestPackageSalePrice").asString


                    } else {

                        Toast.makeText(
                            this@CartActivity,
                            response.body()!!["Msg"].toString().replace("\"", ""),
                            Toast.LENGTH_SHORT
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

    //    {"Status":"true","data":[{"PaymentTransactionID":92,"OrderID":"TP190905112816426","PaymentAmount":"100"}],"Msg":"Generate New payment Order ID "}
    fun callCheckout() {

        if (!DialogUtils.isNetworkConnected(this@CartActivity)) {
            Utils.ping(this@CartActivity, "Connection not available")
        }

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.checkout(
            Utils.getStringValue(this@CartActivity, AppConstants.USER_ID, "0")!!
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    if (response.body()!!["Status"].asString == "true") {

                        if (!response.body()!!["data"].asJsonArray[0].asJsonObject["PaymentAmount"].asString.equals(
                                "0",
                                true
                            )
                        ) {

                            Log.v(
                                "order_id: ",
                                "" + response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].asString
                            )

                            val intent =
                                Intent(this@CartActivity, TraknpayRequestActivity::class.java)
                            intent.putExtra(
                                "order_id",
                                response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].asString
                            )
                            intent.putExtra(
                                "amount",
                                response.body()!!["data"].asJsonArray[0].asJsonObject["PaymentAmount"].asString
                            )
//                            intent.putExtra("pkgid", pkgid)
                            intent.putExtra("pkgname", cart_tvPname.text.toString())
//                            intent.putExtra("pkgprice", purchaseCoin)
                            startActivity(intent)
//                            (context as DashboardActivity).finish()

                        } else {
                            updatePaymentStatus(
                                "Success",
                                response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].asString
                            )
                        }

                    } else {

                        Toast.makeText(
                            this@CartActivity,
                            response.body()!!["Msg"].toString().replace("\"", ""),
                            Toast.LENGTH_SHORT
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

    fun updatePaymentStatus(transaction_status: String, order_id: String) {
        if (!DialogUtils.isNetworkConnected(this@CartActivity)) {
            Utils.ping(this@CartActivity, "Connection not available")
        }

        DialogUtils.showDialog(this@CartActivity)

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.updatePaymentStatus(
            WebRequests.getPaymentStatusParams(
                Utils.getStringValue(this@CartActivity, AppConstants.USER_ID, "")!!,
                order_id,
                "0",
                transaction_status
            )
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    DialogUtils.dismissDialog()

                    AppConstants.isFirst = 1

                    val intent = Intent(this@CartActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
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
