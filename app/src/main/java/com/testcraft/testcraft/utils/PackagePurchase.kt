package com.testcraft.testcraft.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.testcraft.testcraft.activity.QuestionInstructionActivity
import com.testcraft.testcraft.models.TestListModel
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackagePurchase {

    companion object {

        var stuGUID = ""

        //    {"Status":"true","data":"24","Msg":"Package added into cart"}
        fun callAddToCart(come_from: String, pkgid: String, context: Context, ccode: String) {

            if (!DialogUtils.isNetworkConnected(context)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
                DialogUtils.NetworkDialog(context)
                DialogUtils.dismissDialog()
            }

            DialogUtils.showDialog(context)
            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call = apiService.addToCart(
                Utils.getStringValue(context, AppConstants.USER_ID, "0")!!,
                pkgid
            )

            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.body() != null) {

                        if (response.body()!!["Status"].asString == "true") {

                            stuGUID = response.body()!!["data"].asString

//                        fragmentManager!!.beginTransaction().replace(R.id.container, ChooseMarketPlaceFragment()).commit()

//                        Toast.makeText(
//                            context,
//                            response.body()!!["Msg"].toString().replace("\"", ""),
//                            Toast.LENGTH_SHORT
//                        ).show()

//                        val intent = Intent(activity, CartActivity::class.java)
//                        startActivity(intent)
//
                            callCheckout(come_from, context, ccode)

                        } else {

                            DialogUtils.dismissDialog()

                            Toast.makeText(
                                context,
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
        fun callCheckout(come_from: String, context: Context, ccode: String) {

            if (!DialogUtils.isNetworkConnected(context)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
                DialogUtils.NetworkDialog(context)
                DialogUtils.dismissDialog()
            }

            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call = apiService.checkout(
                Utils.getStringValue(context, AppConstants.USER_ID, "0")!!, ccode
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

//                            val intent = Intent("com.testcraft.testcraft")
//                            intent.addCategory(Intent.CATEGORY_DEFAULT)
//                            intent.addCategory(Intent.CATEGORY_BROWSABLE)
//                            val bundle = Bundle()
//                            bundle.putString("msg_from_browser", "Launched from Browser")
//                            intent.putExtras(bundle)
//
//                            Log.d("mobikul-->", intent.toUri(Intent.URI_INTENT_SCHEME))

                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(AppConstants.PAYMENT_REQUEST + "StudentID=$stuGUID&type=2&subcription=0")
                                )

                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                browserIntent.setPackage("com.android.chrome")
                                try {
                                    context.startActivity(browserIntent)
                                } catch (ex: ActivityNotFoundException) {
                                    // Chrome browser presumably not installed so allow user to choose instead
                                    browserIntent.setPackage(null)
                                    context.startActivity(browserIntent)
                                }

//                            startActivity(browserIntent)

//                            val intent = Intent(context, TraknpayRequestActivity::class.java)
//                            intent.putExtra(
//                                "order_id",
//                                response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].asString
//                            )
//                            intent.putExtra(
//                                "amount",
//                                response.body()!!["data"].asJsonArray[0].asJsonObject["PaymentAmount"].asString
//                            )
//                            intent.putExtra("pkgid", pkgid)
//                            intent.putExtra("pkgname", package_detail_tvPname.text.toString())
//                            intent.putExtra("pkgprice", purchaseCoin)
//                            startActivity(intent)

//                            (context as DashboardActivity).finish()

                            } else {
                                updatePaymentStatus(come_from,
                                    "Success",
                                    response.body()!!["data"].asJsonArray[0].asJsonObject["OrderID"].asString
                                , context)
                            }

                        } else {

                            Toast.makeText(
                                context,
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

        fun updatePaymentStatus(come_from: String, transaction_status: String, order_id: String, context: Context) {

            if (!DialogUtils.isNetworkConnected(context)) {
//            Utils.ping(activity!!, AppConstants.NETWORK_MSG)
                DialogUtils.NetworkDialog(context)
                DialogUtils.dismissDialog()
            }

            DialogUtils.showDialog(context)

            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call = apiService.updatePaymentStatusNew(
                WebRequests.getPaymentStatusParams(
                    Utils.getStringValue(context, AppConstants.USER_ID, "")!!,
                    order_id,
                    "0",
                    transaction_status
                )
            )

            call.enqueue(object : Callback<TestListModel> {
                override fun onResponse(
                    call: Call<TestListModel>,
                    response: Response<TestListModel>
                ) {

                    if (response.body() != null) {

                        DialogUtils.dismissDialog()

                        if (come_from == "deeplink") {
                            Utils.setStringValue(context, AppConstants.IS_DEEPLINK_STEP, "2")
                        }

                        val intent = Intent(context, QuestionInstructionActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

                        intent.putExtra("isComeFrom", come_from)

                        intent.putExtra("testid", response.body()!!.data[0].TestID.toString())
                        intent.putExtra(
                            "studenttestid",
                            response.body()!!.data[0].StudentTestID.toString()
                        )
                        intent.putExtra("testname", response.body()!!.data[0].TestName)
                        intent.putExtra("testtime", response.body()!!.data[0].RemainTime)
                        intent.putExtra("totalque", response.body()!!.data[0].TotalQuestions)
                        intent.putExtra("subjectname", response.body()!!.data[0].SubjectName)
                        intent.putExtra("coursename", response.body()!!.data[0].CourseName)
                        intent.putExtra("totalmarks", response.body()!!.data[0].TestMarks)
                        intent.putExtra("tutorname", response.body()!!.data[0].TutorName)
                        intent.putExtra("totalhint", response.body()!!.data[0].NumberOfHint)
                        intent.putExtra("totalhintused", response.body()!!.data[0].NumberOfHintUsed)
                        intent.putExtra(
                            "que_instruction",
                            response.body()!!.data[0].TestInstruction
                        )
                        context.startActivity(intent)
                        (context as Activity).finish()

//                    AppConstants.isFirst = 1
//                    setFragments(null)

//                    if (response.body()!!["Status"].asString == "true") {

//                        if (transaction_status == "Success") {

//                            Toast.makeText(
//                                context,
//                               "Free package buy successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()

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


//                    AppConstants.isFirst = 1
//
//                    val intent = Intent(context, DashboardActivity::class.java)
//                    startActivity(intent)

//                        Toast.makeText(context, response.body()!!["Msg"].asString, Toast.LENGTH_LONG)
//                            .show()


//                    }
                    }
                }

                override fun onFailure(call: Call<TestListModel>, t: Throwable) {
                    // Log error here since request failed
                    Log.e("", t.toString())
                    DialogUtils.dismissDialog()
                }
            })

        }
    }
}
