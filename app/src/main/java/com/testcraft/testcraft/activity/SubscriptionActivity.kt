package com.testcraft.testcraft.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.testcraft.testcraft.Connectivity
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.SubscriptionSubjectAdapter
import com.testcraft.testcraft.interfaces.SubscriptionInterface
import com.testcraft.testcraft.models.GetSubscriptionModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.activity_subscription.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class SubscriptionActivity : AppCompatActivity(), SubscriptionInterface {

    var connectivity: Connectivity? = null
    var iinterface: SubscriptionInterface? = null

    companion object {
        var subscriptionSubjectArr: ArrayList<String> = ArrayList()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iinterface = this

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_subscription)

        connectivity = Connectivity()

        subscription_btnConfirm.visibility = View.GONE

        subscription_rvSelect.layoutManager =
            LinearLayoutManager(this@SubscriptionActivity, LinearLayoutManager.HORIZONTAL, false)

        subscription_ivPlus.setOnClickListener {

            val intent = Intent(this@SubscriptionActivity, NewActivity::class.java)
            intent.putExtra("comeadater", "subscription")
            startActivity(intent)
            finish()
        }

        subscription_btnConfirm.setOnClickListener {

            DialogUtils.createConfirmDialog(
                this@SubscriptionActivity,
                "Confirm?",
                "Are you sure you want to buy this subscription?",
                "Yes",
                "No",
                DialogInterface.OnClickListener { dialog, which ->
                    callGetSubscriptionConfirm()
                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()

                }).show()
        }

        subscription_ivBack.setOnClickListener { finish() }

        callGetSubscription()
//        callGetSubscriptionCount()

    }

    override fun onBackPressed() {

        AppConstants.isFirst = 0

        val intent = Intent(this@SubscriptionActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun callGetSubscription() {
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getStudentSubscription(
            Utils.getStringValue(this@SubscriptionActivity, AppConstants.USER_ID, "0")!!)

        call.enqueue(object : Callback<GetSubscriptionModel> {

            override fun onResponse(call: Call<GetSubscriptionModel>, response: Response<GetSubscriptionModel>) {

                if (response.body()!!.data != null) {

                    if (response.body()!!.data.size > 0) {

                        subscription_btnConfirm.visibility = View.VISIBLE

                        callGetSubscriptionCount()

                        subscription_rvSelect.visibility = View.VISIBLE

                        subscription_rvSelect.adapter =
                            SubscriptionSubjectAdapter(this@SubscriptionActivity, response.body()!!.data, iinterface!!)
                    } else {

                        subscription_btnConfirm.visibility = View.GONE
                        subscription_cvPrice.visibility = View.GONE

                        subscription_rvSelect.visibility = View.GONE
                        subscription_tvPoints.text =
                            "• Become a TC member \n\n• Get access to unlimited tests \n\n• Create your own test \n\n• Discounts on curated tests"
                    }
                } else {

                    subscription_cvPrice.visibility = View.GONE

                    subscription_btnConfirm.visibility = View.GONE
                    subscription_rvSelect.visibility = View.GONE
                    subscription_tvPoints.text =
                        "• Become a TC member \n\n• Get access to unlimited tests \n\n• Create your own test \n\n• Discounts on curated tests"

                }
            }

            override fun onFailure(call: Call<GetSubscriptionModel>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

//    fun callCheckout() {
//
//        if (!DialogUtils.isNetworkConnected(this@SubscriptionActivity)) {
//            Utils.ping(this@SubscriptionActivity, AppConstants.NETWORK_MSG)
//        }
//
//        val apiService = WebClient.getClient().create(WebInterface::class.java)
//
//        val call =
//            apiService.subscription_checkout(Utils.getStringValue(this@SubscriptionActivity, AppConstants.USER_ID, "0")!!)
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
//                        Log.d("url", Uri.parse(AppConstants.PAYMENT_REQUEST + "StudentID="+ Utils.getStringValue(this@SubscriptionActivity, AppConstants.USER_ID, "0")!! + "&subcription=1")
//                            .toString())
//
//                        val browserIntent = Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse(AppConstants.PAYMENT_REQUEST + "StudentID="+ Utils.getStringValue(this@SubscriptionActivity, AppConstants.USER_ID, "0")!! + "&subcription=1")
//                        )
//
//                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        browserIntent.setPackage("com.android.chrome")
//                        try {
//                            startActivity(browserIntent)
//                        } catch (ex: ActivityNotFoundException) {
//                            // Chrome browser presumably not installed so allow user to choose instead
//                            browserIntent.setPackage(null)
//                            startActivity(browserIntent)
//                        }
//
//                    } else {
//
//                        Toast.makeText(
//                            this@SubscriptionActivity,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                    }
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

    fun callGetSubscriptionCount() {
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getSubscriptionCount(
            Utils.getStringValue(this@SubscriptionActivity, AppConstants.USER_ID, "0")!!)

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!.get("data").asJsonObject != null) {

                    subscription_cvPrice.visibility = View.VISIBLE

                    subscription_tvPoints.text =
                        "• " + response.body()!!.get("data").asJsonObject.get("Question").asString +
                                " enriched question bank with explanation and hints \n\n• " +
                                response.body()!!.get("data").asJsonObject.get("TestPackage").asString +
                                " curated papers by expert TC curators \n\n• Discounts on tests created by reputed curators"

                    subscription_tvPrice.text =
                        "₹ " + response.body()!!.get("data").asJsonObject.get("Price").asString
                    subscription_tvListPrice.text =
                        "₹ " + response.body()!!.get("data").asJsonObject.get("ListPrice").asString
                    subscription_tvListPrice.paintFlags =
                        subscription_tvListPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    Utils.setFont(this@SubscriptionActivity, "fonts/Inter-Bold.ttf", subscription_tvPrice)

                } else {

                    subscription_cvPrice.visibility = View.GONE

                    subscription_tvPoints.text =
                        "• Become a TC member \n\n• Get access to unlimited tests \n\n• Create your own test \n\n• Discounts on curated tests"
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

    fun callRemoveSubscription(cource_type_id: String, cource_id: String, board_id: String, std_id: String) {
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.removeSubscriptionSubject(
            Utils.getStringValue(this@SubscriptionActivity, AppConstants.USER_ID, "0")!!,
            cource_id,
            board_id,
            std_id,
            cource_type_id)

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                callGetSubscription()

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

    override fun getRemove(cource_type_id: String, cource_id: String, std_id: String, board_id: String) {

        callRemoveSubscription(cource_type_id, cource_id, board_id, std_id)

    }

    fun callGetSubscriptionConfirm() {
        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call = apiService.getSubscriptionConfirm(
            Utils.getStringValue(this@SubscriptionActivity, AppConstants.USER_ID, "0")!!)

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body() != null) {

                    if (response.body()!!["Status"].asString == "true") {

//                        if (response.body()!!["data"].asJsonArray[0].asJsonObject["IsFree"].asString == "0") {
//                            Log.d("url", Uri.parse(AppConstants.PAYMENT_REQUEST + "StudentID=" + Utils.getStringValue(this@SubscriptionActivity, AppConstants.USER_ID, "0")!! + "&subcription=1")
//                                .toString())
//
//                            val browserIntent = Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse(AppConstants.PAYMENT_REQUEST + "StudentID=" + Utils.getStringValue(this@SubscriptionActivity, AppConstants.USER_ID, "0")!! + "&subcription=1")
//                            )
//
//                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            browserIntent.setPackage("com.android.chrome")
//                            try {
//                                startActivity(browserIntent)
//                            } catch (ex: ActivityNotFoundException) {
//                                // Chrome browser presumably not installed so allow user to choose instead
//                                browserIntent.setPackage(null)
//                                startActivity(browserIntent)
//                            }
//                        } else {
                        AppConstants.isFirst = 0
                        val intent =
                            Intent(this@SubscriptionActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
//                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

}
