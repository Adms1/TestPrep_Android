package com.testcraft.testcraft.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommonWebCalls {

    companion object {

        fun callToken(context: Context) {
            if (!DialogUtils.isNetworkConnected(context)) {
                Utils.ping(context, "Connetion not available")
            }

            DialogUtils.showDialog(context)

            val apiService = WebClient.getClient().create(WebInterface::class.java)

            val call =
                apiService.getToken("http://izumlabs.com/getsession.asp?gameid=3371B09E-B7E2-4327-B072-A01559365660")

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    if (response.body() != null) {

                        DialogUtils.dismissDialog()

//                        if (response.body()!!["Status"].asString == "true") {

                        var token = response.body().toString()
                        Log.i("ADASDASDASD", response.body().toString())

//                        } else {
//                            Log.e("fail", "fail")
//                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Log error here since request failed
                    Log.e("", t.toString())
                    DialogUtils.dismissDialog()
                }
            })
        }

        fun callReportIssue(context: Context, issueType: String, typename: String, qid: String) {

            if (!DialogUtils.isNetworkConnected(context)) {
                Utils.ping(context, "Connetion not available")
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
