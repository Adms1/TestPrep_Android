package com.testcraft.testcraft.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.AttemptModel

class QuestionAttemptAdapter(
    val context: Context,
    val dataList: ArrayList<AttemptModel.Attemptdata>
) :
    RecyclerView.Adapter<QuestionAttemptAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.attempt_list_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.section.text = dataList[p1].SectionName
        p0.attempt.text = dataList[p1].Answered
        p0.notattempt.text = dataList[p1].UnAnswered

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var section: TextView = itemView.findViewById(R.id.attempt_list_tvSection)
        var attempt: TextView = itemView.findViewById(R.id.attempt_list_tvAttempted)
        var notattempt: TextView = itemView.findViewById(R.id.attempt_list_tvNotAttempted)

    }
}
