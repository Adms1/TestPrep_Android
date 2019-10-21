package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import com.testprep.models.TutorModel

class TutorReviewAdapter(val context: Context, val dataList: ArrayList<TutorModel.TutorData>) :
    RecyclerView.Adapter<TutorReviewAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(com.testprep.R.layout.list_item_tutor_review, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.tutor_name.text = dataList[p1].StudentName
        p0.tutor_sname.text = dataList[p1].StudentName.substring(0, 1).toUpperCase()
        p0.remark.text = dataList[p1].Remarks
        p0.date.text = dataList[p1].DateTime
        p0.rating.rating = dataList[p1].Rating.toFloat()

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tutor_name: TextView = itemView.findViewById(com.testprep.R.id.tutor_item_tvName)
        var tutor_sname: TextView = itemView.findViewById(com.testprep.R.id.tutor_review_tvLogo)
        var remark: TextView = itemView.findViewById(com.testprep.R.id.tutor_item_tvDesc)
        var date: TextView = itemView.findViewById(com.testprep.R.id.tutor_item_tvDate)
        var rating: RatingBar = itemView.findViewById(com.testprep.R.id.tutor_review_rating)

    }

}
