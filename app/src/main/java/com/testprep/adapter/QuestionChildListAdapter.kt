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
    val filterTypeSelectionInteface: FilterTypeSelectionInteface,
    val come_from: String

) : RecyclerView.Adapter<QuestionChildListAdapter.viewholder>() {

    var old_que: Int? = null

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

        old_que = AppConstants.QUE_NUMBER

        p0.title.setOnClickListener {

            if (come_from == "question") {
//                dataList[old_que!!].type = 3
//                dataList[p1].type = 1

                AppConstants.QUE_NUMBER = p1

            } else {

                AppConstants.QUE_NUMBER1 = p1
            }

//            old_type = dataList[old_que!!].type

//            p0.title.setTextColor(context.resources.getColor(R.color.white))
//            p0.title.setBackgroundResource(R.drawable.red_round)

            notifyDataSetChanged()

            Log.d("que_number", "" + AppConstants.QUE_NUMBER)

            filterTypeSelectionInteface.getType("adapter", p1)

        }

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
                p0.title.setTextColor(context.resources.getColor(R.color.gray))
                p0.title.setBackgroundResource(R.drawable.light_gray_round_bg)
            }
        }

        if (come_from == "question") {
            if (AppConstants.QUE_NUMBER == p1) {
                p0.title.setTextColor(context.resources.getColor(R.color.white))
                p0.title.setBackgroundResource(R.drawable.blue_round)
            }
        } else {
            if (AppConstants.QUE_NUMBER1 == p1) {
                p0.title.setTextColor(context.resources.getColor(R.color.white))
                p0.title.setBackgroundResource(R.drawable.blue_round)
            }
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.queList_tvChild)

    }
}

