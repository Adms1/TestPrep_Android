package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testprep.R
import com.testprep.models.GetCourseListData

class NewSelectSubjectAdapter(val context: Context, val dataList: ArrayList<GetCourseListData>) :
    RecyclerView.Adapter<NewSelectSubjectAdapter.viewholder>() {

    var row_index = -1
    var selected: ArrayList<Int> = ArrayList()

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

//        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].SubjectName.substring(0, 1)))
        p0.title.text = dataList[p1].SubjectName
        p0.stitle.text = p0.title.text.substring(0, 1)

        p0.image.setOnClickListener {

            row_index = p1

            notifyDataSetChanged()

        }

        if (row_index == p1) {

            p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))
            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
            p0.p_select.visibility = View.VISIBLE

            if (selected.size > 0) {

                for (i in 0..selected.size) {
                    if (selected.contains(dataList[p1].SubjectID)) {
                        selected.remove(dataList[p1].SubjectID)
                        p0.p_select.visibility = View.GONE
                        p0.title.setTextColor(context.resources.getColor(R.color.light_gray))
                        p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
                        break

                    } else {
                        selected.add(dataList[p1].SubjectID)

                        break

                    }
                }

                Log.d("selected array : ", " $selected")

            } else {
                selected.add(dataList[p1].SubjectID)
            }

        } else {
//            p0.title.setTextColor(context.resources.getColor(R.color.black))
//            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))

        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.package_image)
        var title: TextView = itemView.findViewById(R.id.package_name)
        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
        var p_select: ImageView = itemView.findViewById(R.id.package_select)

    }

    fun getIds(): String {

        val ids = selected.toString().replace("[", "").replace("]", "")

        return ids

    }


}
