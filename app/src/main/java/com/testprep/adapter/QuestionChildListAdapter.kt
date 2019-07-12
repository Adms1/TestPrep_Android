package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.interfaces.FilterTypeSelectionInteface

class QuestionChildListAdapter(
    val context: Context,
    val dataList: ArrayList<String>,
    val filterTypeSelectionInteface: FilterTypeSelectionInteface
) :
    RecyclerView.Adapter<QuestionChildListAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.question_list_child,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {
        p0.title.text = (p1 + 1).toString()

        p0.title.setOnClickListener {

            p0.title.setTextColor(context.resources.getColor(R.color.white))
            p0.title.setBackgroundResource(R.drawable.pink_round)

            filterTypeSelectionInteface.getType(p1)
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.queList_tvChild)

    }
}

