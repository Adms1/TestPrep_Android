package com.testcraft.testcraft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.TemplateSectionModel

class TemplateSectionAdapter(
    val context: Context,
    val dataList: ArrayList<TemplateSectionModel.TemplateSetionData>) :
    RecyclerView.Adapter<TemplateSectionAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.template_section_list_view,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.section.text = dataList[p1].SubjectName
        p0.qtype.text = dataList[p1].QuestionTypeName
//        p0.no.text = dataList[p1].NoOfQue
        p0.marks.text = dataList[p1].NoOfQue + "*" + dataList[p1].Marks
        p0.total.text = (dataList[p1].NoOfQue.toDouble() * dataList[p1].Marks.toDouble()).toString()

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var section: TextView = itemView.findViewById(R.id.section_list_tvSection)
        var qtype: TextView = itemView.findViewById(R.id.section_list_tvQtype)

        //        var no: TextView = itemView.findViewById(R.id.section_list_tvNo)
        var marks: TextView = itemView.findViewById(R.id.section_list_tvMarks)
        var total: TextView = itemView.findViewById(R.id.section_list_tvTotal)
    }

}
