package com.testcraft.testcraft.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DeeplinkTestActivity
import com.testcraft.testcraft.models.DeepLinkModel
import com.testcraft.testcraft.utils.PackagePurchase

class DeepLinkSubjectAdapter(val context: Context, val dataList: ArrayList<DeepLinkModel.DeepLinkData>) :
    RecyclerView.Adapter<DeepLinkSubjectAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.item_layout_deeplink,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.code.text = dataList[p1].language

        p0.cl.setOnClickListener {
            if (dataList[p1].purchased != "1") {
                PackagePurchase.callAddToCart("deeplink", dataList[p1].TestPackageID, context, dataList[p1].couponcode)
            } else {

                val i = Intent(context, DeeplinkTestActivity::class.java)
                context.startActivity(i)
            }
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var code: TextView = itemView.findViewById(R.id.item_deeplink_tvSubject)
        var cl: ConstraintLayout = itemView.findViewById(R.id.item_deeplink_llMain)

    }
}
