package com.testcraft.testcraft.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.ViewInvoiceActivity
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.AppConstants

@SuppressLint("SetTextI18n")
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

}
