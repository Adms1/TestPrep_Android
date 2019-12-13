package com.testcraft.testcraft.adapter

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.Utils

class RecentSearchAdapter(val context: Context, val dataList: List<String>) :
    RecyclerView.Adapter<RecentSearchAdapter.viewholder>() {

    var row_index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
//            LayoutInflater.from(context).inflate(R.layout.list_item_test_package, p0, false)

            LayoutInflater.from(context).inflate(R.layout.recent_search_list_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.name.text = dataList[p1]

        p0.name.setOnClickListener {

            AppConstants.isFirst = 13
            val bundle = Bundle()
            bundle.putString("type", "explore")
            bundle.putString("pname1", "Packages")
            bundle.putString("course_type", "")
            bundle.putString("boardid", "")
            if (Utils.getStringValue(context, AppConstants.COURSE_TYPE_ID, "0")!! == "1") {

                bundle.putString(
                    "stdid",
                    Utils.getStringValue(context, AppConstants.STANDARD_ID, "0")
                )

            } else {
                bundle.putString("stdid", "")
            }
            bundle.putString("subid", "")
            bundle.putString("tutorid", "")
            bundle.putString("search_name", dataList[p1])
            bundle.putString("maxprice", "")
            bundle.putString("minprice", "")
            bundle.putString("filtertypeid", "-1")
            setFragments(bundle)
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.recent_item_ivName)

    }

}

