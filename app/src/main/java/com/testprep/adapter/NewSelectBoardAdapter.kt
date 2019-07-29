package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.activity.PrefrenceActivity
import com.testprep.models.FilterModel
import com.testprep.utils.AppConstants
import com.testprep.utils.Utils

class NewSelectBoardAdapter(val context: Context, val dataList: ArrayList<FilterModel.FilterData>) :
    RecyclerView.Adapter<NewSelectBoardAdapter.viewholder>() {

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

//      p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].CourseName.substring(0, 1)))

        p0.title.text = dataList[p1].CourseName
//        p0.stitle.text = p0.title.text.substring(0, 1)

        if (dataList[p1].Icon != null) {
            Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon).into(p0.image)
        } else {
//            p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].CourseName.substring(0, 1)))
            p0.stitle.text = p0.title.text.substring(0, 1)

        }

        p0.image.setOnClickListener {

            Utils.setStringValue(context, "course_id", dataList[p1].CourseID.toString())
            Utils.setStringValue(context, "course_name", dataList[p1].CourseName)

            row_index = p1
            notifyDataSetChanged()

            p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))

            if (Utils.getStringValue(context, "course_type_id", "") == "1") {
                (context as PrefrenceActivity).callStandardList(dataList[p1].CourseID.toInt())
            } else {
                (context as PrefrenceActivity).callSubjectList(dataList[p1].CourseID.toString())
            }
            notifyDataSetChanged()

        }

        if (dataList[p1].Icon == null) {
            if (row_index == p1) {
                p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))
                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
                p0.p_select.visibility = View.VISIBLE
            } else {
                p0.title.setTextColor(context.resources.getColor(R.color.black))
                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
                p0.p_select.visibility = View.GONE
            }
        } else {
            if (row_index == p1) {
                p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))
//                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
                p0.p_select.visibility = View.VISIBLE
            } else {
                p0.title.setTextColor(context.resources.getColor(R.color.black))
//                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
                p0.p_select.visibility = View.GONE
            }
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.package_image)
        var title: TextView = itemView.findViewById(R.id.package_name)
        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
        var p_select: ImageView = itemView.findViewById(R.id.package_select)
    }
}
