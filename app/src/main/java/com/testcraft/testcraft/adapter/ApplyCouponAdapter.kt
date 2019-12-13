package com.testcraft.testcraft.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.CouponModel

class ApplyCouponAdapter(val context: Context, val dataList: ArrayList<CouponModel.dataa>) :
    RecyclerView.Adapter<ApplyCouponAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.applycoupon_list_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.code.text = dataList[p1].CouponCode
        p0.title.text = dataList[p1].CouponTitle
        p0.desc.text = dataList[p1].CouponDiscription
//        p0.date.text = dataList[p1].ExpireDate

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var code: TextView = itemView.findViewById(R.id.coupon_tvCode)
        var title: TextView = itemView.findViewById(R.id.coupon_tvTitle)
        var desc: TextView = itemView.findViewById(R.id.coupon_tvDesc)
        var date: TextView = itemView.findViewById(R.id.coupon_tvExpiry)

    }
}
