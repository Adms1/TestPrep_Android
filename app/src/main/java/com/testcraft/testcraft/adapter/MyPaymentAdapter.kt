package com.testcraft.testcraft.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.TraknpayRequestActivity
import com.testcraft.testcraft.activity.ViewInvoiceActivity
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import com.testcraft.testcraft.utils.Utils
import com.testcraft.testcraft.utils.WebRequests
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPaymentAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
    RecyclerView.Adapter<MyPaymentAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_my_payment, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        if (dataList[p1].ExternalTransactionStatus.equals("Success", true)) {
//            p0.status.setTextColor(context.resources.getColor(R.color.green))
//        } else {
//            p0.status.setTextColor(context.resources.getColor(R.color.red))
//        }

        p0.date.text = "Date : " + dataList[p1].PaymentDate
        p0.id.text = "Package : " + dataList[p1].PackageName
//        p0.status.text = dataList[p1].ExternalTran sactionStatus

        if (dataList[p1].IsFree == "1") {
            p0.amount.text = "Free"
        } else {
            p0.amount.text = "₹ " + dataList[p1].PaymentAmount.toString()
        }

        p0.invoice.setOnClickListener {

            val intent = Intent(context, ViewInvoiceActivity::class.java)
            intent.putExtra(
                "url",
                AppConstants.INVOICE_URL + dataList[p1].InvoiceGUID
//                "https://docs.google.com/viewer?embedded=true&url=" + AppConstants.INVOICE_URL + dataList[p1].InvoiceGUID
            )
            intent.putExtra("header", "Invoice")
            context.startActivity(intent)
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var date: TextView = itemView.findViewById(R.id.item_my_payment_date)
        var id: TextView = itemView.findViewById(R.id.item_my_payment_id)
        //        var status: TextView = itemView.findViewById(R.id.item_my_payment_status)
        var amount: TextView = itemView.findViewById(R.id.item_my_payment_amount)
        var invoice: TextView = itemView.findViewById(R.id.item_my_payment_invoice)
    }

    fun generateTrackNPayRequest(coin: String, trans_id: String) {
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

        val call = apiService.getPayment(
            WebRequests.getPaymentParams(
                trans_id,
                Utils.getStringValue(context, AppConstants.USER_ID, "")!!,
                coin
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
//                        intent.putExtra("pkgid", pkgid)
//                        intent.putExtra("pkgname", tvPkgname.text.toString())
//                        intent.putExtra("pkgprice", purchaseCoin)
                        context.startActivity(intent)

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

}
