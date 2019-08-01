package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.interfaces.FilterTypeSelectionInteface

class FilterListAdapter(
    val context: Context,
    val dataList: ArrayList<String>,
    var filterTypeSelectionInteface: FilterTypeSelectionInteface
) :
    RecyclerView.Adapter<FilterListAdapter.viewholder>() {

    var row_index = 0

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.filter_type_list_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {
        p0.title.text = dataList[p1]

        p0.title.setOnClickListener {
            filterTypeSelectionInteface.getType(p1)

            row_index = p1
            notifyDataSetChanged()
        }

        if (row_index == p1) {
            p0.title.setTextColor(context.resources.getColor(R.color.white))
            p0.title.setBackgroundColor(context.resources.getColor(R.color.colorPrimaryDark))

        } else {
            p0.title.setTextColor(context.resources.getColor(R.color.gray))
            p0.title.setBackgroundColor(context.resources.getColor(R.color.light_blue))

        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.filter_item_tvType)

    }
}
