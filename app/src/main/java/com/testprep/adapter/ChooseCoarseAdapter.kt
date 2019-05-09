package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R

class ChooseCoarseAdapter(val context: Context, val dataList: Array<String>) :
    RecyclerView.Adapter<ChooseCoarseAdapter.viewholder>() {

    var row_index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_drawer_menu,
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

            row_index = p1
            notifyDataSetChanged()

        }

        if (row_index == p1) {
            p0.title.setBackgroundResource(R.drawable.dark_blue_gredient_bg)
        } else {
            p0.title.setBackgroundResource(R.drawable.blue_gradient_bg)
        }


    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.drawer_list_title)

    }
}

