package com.testcraft.testcraft.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.testcraft.testcraft.activity.*
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommonWebCalls {

    companion object {

        fun callToken(
            context: Context,
            type: String,
            gameid: String,
            Actionid: String,
            comment: String
        ) {
//            DialogUtils.showDialog(context)

            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call = apiService.getToken(
                WebRequests.getAction(
                    type,
                    gameid,
                    Utils.getTokenPref(context, AppConstants.DEFAULT_ACTION_ID, "")!!,
                    Actionid,
                    comment
                )
            )

            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {

                    if (response.body() != null) {

//                        DialogUtils.dismissDialog()

                        if (response.body()!!["Status"].asString == "true") {

                            if (type == "0") {
                                Utils.setTokenPref(
                                    context,
                                    AppConstants.DEFAULT_ACTION_ID,
                                    response.body()!!["data"].asString
                                )

                                Log.d(
                                    "default acid common",
                                    Utils.getTokenPref(
                                        context,
                                        AppConstants.DEFAULT_ACTION_ID,
                                        ""
                                    )!!
                                )

                                callToken(context, "1", "", ActionIdData.C100, ActionIdData.T100)

                            }
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Log error here since request failed
                    Log.e("", t.toString())
//                    DialogUtils.dismissDialog()
                }
            })
        }

        fun callSignupApi(
            comefrom: String,
            context: Context,
            accType: String,
            stuid: String,
            fname: String,
            lname: String,
            email: String,
            pass: String,
            phone: String
        ) {

            if (!DialogUtils.isNetworkConnected(context)) {
                Utils.ping(context, AppConstants.NETWORK_MSG)
            }

            val apiService = WebClient.getClient().create(WebInterface::class.java)

            DialogUtils.showDialog(context)

            val call = apiService.getSignup(
                WebRequests.addSignupParams(accType, stuid, fname, lname, email, pass, phone, Utils.getDeviceId(context))
            )
//            val call = apiService.getSignup(
//                WebRequests.addSignupParams(accType, stuid, fname, lname, email, pass, phone, "testdeviceid4")
//            )

            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.body() != null) {

                        DialogUtils.dismissDialog()

                        if (response.body()!!.get("Status").asString == "true") {

                            if (response.body()!!["data"].asJsonArray.size() > 0) {
                                Utils.setStringValue(
                                    context,
                                    AppConstants.FIRST_NAME,
                                    response.body()!!["data"].asJsonArray[0].asJsonObject["StudentFirstName"].asString
                                )
                                Utils.setStringValue(
                                    context,
                                    AppConstants.LAST_NAME,
                                    response.body()!!["data"].asJsonArray[0].asJsonObject["StudentLastName"].asString
                                )
                                Utils.setStringValue(
                                    context,
                                    AppConstants.USER_ID,
                                    response.body()!!["data"].asJsonArray[0].asJsonObject["StudentID"].asString
                                )
                                Utils.setStringValue(
                                    context,
                                    AppConstants.USER_EMAIL,
                                    response.body()!!["data"].asJsonArray[0].asJsonObject["StudentEmailAddress"].asString
                                )
                                Utils.setStringValue(
                                    context,
                                    AppConstants.USER_PASSWORD,
                                    response.body()!!["data"].asJsonArray[0].asJsonObject["StudentPassword"].asString
                                )
                                Utils.setStringValue(
                                    context,
                                    AppConstants.USER_MOBILE,
                                    response.body()!!["data"].asJsonArray[0].asJsonObject["StudentMobile"].asString
                                )
                                Utils.setStringValue(
                                    context,
                                    AppConstants.USER_ACCOUNT_TYPE,
                                    response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString
                                )
                                Utils.setStringValue(
                                    context, AppConstants.USER_STATUSID,
                                    response.body()!!["data"].asJsonArray[0].asJsonObject["StatusID"].asString
                                )

                                when (comefrom) {
                                    "intro"    -> {

                                        if (Utils.getStringValue(context, AppConstants.APP_MODE, "") != AppConstants.NORMAL_MODE) {
                                            if (Utils.getStringValue(context, AppConstants.IS_DEEPLINK_STEP, "") == "3") {
                                                (context as ResultActivity).finish()
                                            } else if (Utils.getStringValue(context, AppConstants.IS_DEEPLINK_STEP, "") == "2") {
                                                (context as DeeplinkTestActivity).finish()
                                            } else {
                                                (context as DashboardActivity).finish()
                                            }
                                        } else {
                                            val intent = Intent(context, NewActivity::class.java)
                                            context.startActivity(intent)
                                            (context as IntroActivity).finish()
                                        }

                                        Utils.setStringValue(context, AppConstants.APP_MODE, AppConstants.NORMAL_MODE)

                                        Utils.setStringValue(context, AppConstants.IS_LOGIN, "true")


                                    }
                                    "otp"      -> {

                                        if (Utils.getStringValue(context, AppConstants.APP_MODE, "") != AppConstants.NORMAL_MODE) {
                                            if (Utils.getStringValue(context, AppConstants.IS_DEEPLINK_STEP, "") == "2" || Utils.getStringValue(context, AppConstants.IS_DEEPLINK_STEP, "") == "3") {

                                                AppConstants.isFirst = 0

                                                val intent =
                                                    Intent(context, DashboardActivity::class.java)
                                                context.startActivity(intent)
                                                (context as OtpActivity).finish()

                                            }
                                        } else {
                                            Utils.setStringValue(
                                                context, AppConstants.OTP,
                                                response.body()!!["data"].asJsonArray[0].asJsonObject["OTP"].asString
                                            )
                                        }
                                        Utils.setStringValue(context, AppConstants.APP_MODE, AppConstants.NORMAL_MODE)

                                        Utils.setStringValue(context, AppConstants.IS_LOGIN, "true")

                                    }
                                    //prefrence/NewActivity
                                    "guest"    -> {

                                        if (response.body()!!["data"].asJsonArray[0].asJsonObject["AccountTypeID"].asString != "5") {
                                            Utils.setStringValue(context, AppConstants.APP_MODE, AppConstants.NORMAL_MODE)
                                        } else {
                                            Utils.setStringValue(context, AppConstants.APP_MODE, AppConstants.GUEST_MODE)
                                        }

                                    }
                                    "deeplink" -> {

                                        DeeplinkEntryActivity.getDeeplinkSubject(context)

                                        Utils.setStringValue(context, AppConstants.IS_DEEPLINK_STEP, "1")
                                        Utils.setStringValue(context, AppConstants.APP_MODE, AppConstants.DEEPLINK_MODE)
                                    }
                                }
                            }

                            Log.d("websize", response.body()!!.get("Msg").asString)

                        } else {

                            Toast.makeText(
                                context,
                                response.body()!!.get("Msg").asString,
                                Toast.LENGTH_LONG
                            )
                                .show()

                            Log.d("websize", response.body()!!.get("Msg").asString)
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

        fun callReportIssue(context: Context, issueType: String, typename: String, qid: String) {

            if (!DialogUtils.isNetworkConnected(context)) {
                Utils.ping(context, AppConstants.NETWORK_MSG)
            }

            DialogUtils.showDialog(context)

            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call = apiService.reportIssue(
                WebRequests.addreportIssueParams(
                    issueType,
                    typename,
                    "Android",
                    Utils.getStringValue(context, AppConstants.USER_ID, "0")!!,
                    Utils.getStringValue(
                        context,
                        AppConstants.FIRST_NAME,
                        "0"
                    )!! + " " + Utils.getStringValue(
                        context,
                        AppConstants.LAST_NAME,
                        "0"
                    )!!,
                    qid,
                    ""
                )
            )

            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    DialogUtils.dismissDialog()

                    if (response.body() != null) {

                        Toast.makeText(
                            context,
                            response.body()!!["Msg"].asString,
                            Toast.LENGTH_LONG
                        )
                            .show()
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

}
