package com.testcraft.testcraft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.CouponModel

class UsedCouponAdapter(val context: Context, val dataList: ArrayList<CouponModel.dataa>) :
    RecyclerView.Adapter<UsedCouponAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_used_coupon,
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
        p0.desc.text = dataList[p1].CouponDiscription
        p0.date.text = dataList[p1].ExpireDate

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var code: TextView = itemView.findViewById(R.id.list_item_usedcoupon_tvCode)
        var desc: TextView = itemView.findViewById(R.id.list_item_usedcoupon_tvDesc)
        var date: TextView = itemView.findViewById(R.id.list_item_usedcoupon_tvDate)

    }
}
