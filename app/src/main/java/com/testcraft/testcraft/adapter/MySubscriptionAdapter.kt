package com.testcraft.testcraft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.PackageData

class MySubscriptionAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
    RecyclerView.Adapter<MySubscriptionAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_my_subscription, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.date.text = "Date : " + dataList[p1].PaymentDate
        p0.id.text = "Package : " + dataList[p1].PackageName
        p0.amount.text = "₹ " + dataList[p1].PaymentAmount.toString()

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var date: TextView = itemView.findViewById(R.id.item_my_subscription_date)
        var id: TextView = itemView.findViewById(R.id.item_my_subscription_name)
        var amount: TextView = itemView.findViewById(R.id.item_my_subscription_amount)
    }

}
