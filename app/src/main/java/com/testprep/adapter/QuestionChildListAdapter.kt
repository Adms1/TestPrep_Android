package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.QuestionTypeModel
import com.testprep.utils.AppConstants


class QuestionChildListAdapter(
    val context: Context,
    val dataList: ArrayList<QuestionTypeModel>,
    var row_index: Int,
    val filterTypeSelectionInteface: FilterTypeSelectionInteface
) : RecyclerView.Adapter<QuestionChildListAdapter.viewholder>() {

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

        when {
            dataList[p1].type == 1 -> {
                p0.title.setTextColor(context.resources.getColor(R.color.white))
                p0.title.setBackgroundResource(R.drawable.blue_round)
            }
            dataList[p1].type == 2 -> {
                p0.title.setTextColor(context.resources.getColor(R.color.white))
                p0.title.setBackgroundResource(R.drawable.green_round)
            }
            dataList[p1].type == 3 -> {
                p0.title.setTextColor(context.resources.getColor(R.color.white))
                p0.title.setBackgroundResource(R.drawable.red_round)
            }
            dataList[p1].type == 4 -> {
                p0.title.setTextColor(context.resources.getColor(R.color.white))
                p0.title.setBackgroundResource(R.drawable.pink_round)
            }
            dataList[p1].type == 5 -> {
                p0.title.setTextColor(context.resources.getColor(R.color.white))
                p0.title.setBackgroundResource(R.drawable.gray_bg)
            }
        }

        p0.title.setOnClickListener {

            row_index = p1
            notifyDataSetChanged()

            AppConstants.QUE_NUMBER = p1

            Log.d("que_number", "" + AppConstants.QUE_NUMBER)

            filterTypeSelectionInteface.getType(p1)
        }

//        if (row_index == p1) {
//
//            p0.title.setTextColor(context.resources.getColor(R.color.white))
//            p0.title.setBackgroundResource(R.drawable.blue_round)
//        } else {
//
//            p0.title.setTextColor(context.resources.getColor(R.color.gray))
//            p0.title.setBackgroundResource(R.drawable.light_gray_round_bg)
//        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.queList_tvChild)

    }
}

