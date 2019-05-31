package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testprep.R
import com.testprep.activity.PrefrenceActivity
import com.testprep.models.GetCourseListData
import com.testprep.utils.Utils

class NewSelectStandardAdapter(val context: Context, val dataList: ArrayList<GetCourseListData>) :
    RecyclerView.Adapter<NewSelectStandardAdapter.viewholder>() {

    var row_index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.new_prefrence_item_layout,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].StandardName.substring(0, 1)))

        p0.title.text = "Std: " + dataList[p1].StandardName
        p0.stitle.text = dataList[p1].StandardName

        p0.image.setOnClickListener {

            p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))

            row_index = p1
            notifyDataSetChanged()

            (context as PrefrenceActivity).callSubjectList(dataList[p1].StandardID.toString())
            notifyDataSetChanged()
        }

        if (row_index == p1) {
            p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))
            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
            p0.p_select.visibility = View.VISIBLE
        } else {
            p0.title.setTextColor(context.resources.getColor(R.color.black))
            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
            p0.p_select.visibility = View.GONE
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.package_image)
        var title: TextView = itemView.findViewById(R.id.package_name)
        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
        var p_select: ImageView = itemView.findViewById(R.id.package_select)
    }
}
