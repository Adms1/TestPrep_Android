package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.testprep.R
import com.testprep.models.FilterModel


class RecyclerviewAdapter(
    var cotext: Context,
    private val mModelList: ArrayList<FilterModel.FilterData>?,
    val selectionType: String,
    val filterType: String
) :
    RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder>() {

    var raw_index = -1
    var str = ""
    var strID = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.dialog_filter_item_layout,
                parent,
                false
            )
        )
//        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val model = mModelList!![position]

        when (filterType) {
            "standard" -> holder.textView.text = model.StandardName
            "subject" -> holder.textView.text = model.SubjectName
            "tutor" -> holder.textView.text = model.TutorName
            "exam" -> holder.textView.text = model.CourseName
            "course_type" -> holder.textView.text = model.CourseTypeName
        }

        holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.right_white_bg))

        if (model.isSelected) {
//            holder.view.setBackgroundColor(cotext.resources.getColor(R.color.colorPrimaryDark))
            holder.img.visibility = View.VISIBLE
        } else {
//            holder.view.setBackgroundColor(Color.WHITE)
            holder.img.visibility = View.GONE
        }

        holder.mainll.setOnClickListener {

            str = ""

            if (selectionType == "single") {

                raw_index = position

                when (filterType) {
                    "standard" -> {
                        strID = mModelList[position].StandardID
                        str = mModelList[position].StandardName
                    }
                    "subject" -> {
                        strID = mModelList[position].SubjectID
                        str = mModelList[position].SubjectName
                    }
                    "tutor" -> {
                        strID = mModelList[position].TutorID
                        str = mModelList[position].TutorName
                    }
                    "exam" -> {
                        strID = mModelList[position].CourseID
                        str = mModelList[position].CourseName
                    }
                }

                notifyDataSetChanged()

            } else {

//                if(model.isSelected){
//                    model.isSelected = false
//                }else{
//                    model.isSelected = true
//                }

                model.isSelected = !model.isSelected

                if (model.isSelected) {
//                holder.view.setBackgroundColor(cotext.resources.getColor(R.color.colorPrimaryDark))
                    holder.img.visibility = View.VISIBLE
                } else {
//                holder.view.setBackgroundColor(Color.WHITE)
                    holder.img.visibility = View.GONE
                }

            }
        }

        if (selectionType == "single") {
            if (raw_index == position) {
                holder.img.visibility = View.VISIBLE
                model.isSelected = !model.isSelected
            } else {
                holder.img.visibility = View.GONE
                model.isSelected = model.isSelected
            }
        }

    }

    override fun getItemCount(): Int {
        return mModelList?.size ?: 0
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view)
        val mainll: RelativeLayout = view.findViewById(R.id.mainrl)
        var img: ImageView = view.findViewById(R.id.imgSelect)
    }

    fun sendArray(): ArrayList<FilterModel.FilterData> {

        return mModelList!!
    }

    fun sendStandard(): String {
        return "$strID-$str"
    }

}
