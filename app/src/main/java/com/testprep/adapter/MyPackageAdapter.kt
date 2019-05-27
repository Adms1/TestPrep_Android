package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testprep.R
import com.testprep.activity.TabwiseQuestionActivity
import com.testprep.utils.Utils

class MyPackageAdapter(val context: Context, val dataList: ArrayList<Int>) :
    RecyclerView.Adapter<MyPackageAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_my_package, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.sdate.text = "24/05/2019"
        p0.edate.text = "-    24/05/2020"
        p0.name.text = "CBSE 9th Social Science"
        p0.short_name.text = p0.name.text.substring(0, 1)

        p0.image.setImageDrawable(
            Utils.createDrawable(
                'C',
                dataList[p1]
            )
        )

        p0.mainll.setOnClickListener {

            val intent = Intent(context, TabwiseQuestionActivity::class.java)
            context.startActivity(intent)

        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var sdate: TextView = itemView.findViewById(R.id.item_my_package_sdate)
        var edate: TextView = itemView.findViewById(R.id.item_my_package_edate)
        var image: ImageView = itemView.findViewById(R.id.item_my_package_image)
        var name: TextView = itemView.findViewById(R.id.item_my_package_name)
        var short_name: TextView = itemView.findViewById(R.id.item_my_package_name_short)
        var mainll: CardView = itemView.findViewById(R.id.item_my_package_main)
    }

}
