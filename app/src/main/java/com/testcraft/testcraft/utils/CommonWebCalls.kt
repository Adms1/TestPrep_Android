package com.testcraft.testcraft.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
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
            if (!DialogUtils.isNetworkConnected(context)) {
                Utils.ping(context, AppConstants.NETWORK_MSG)
            }

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
