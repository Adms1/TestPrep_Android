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
import com.testprep.interfaces.filterInterface
import com.testprep.models.PackageData
import com.testprep.utils.AppConstants

class FilterAdapter(
    var cotext: Context,
    private val mModelList: ArrayList<PackageData.PackageDataList>?,
    val selectionType: String,
    val filterType: String,
    val filterInterface: filterInterface
) :
    RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

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

        if (selectionType == "single") {
            if (model.isSelected && raw_index == -1) {

//                holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.right_blue))
//                holder.img.setBackgroundResource(R.drawable.gray_ring_bg)

                raw_index = position

            } else {
//                holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.white_ring_bg))
//                holder.img.setBackgroundResource(R.drawable.gray_ring_bg)


            }
        }

        holder.mainll.setOnClickListener {


            str = ""

            if (selectionType == "single") {

                raw_index = position

                holder.img.setBackgroundColor(cotext.resources.getColor(R.color.white))

                when (filterType) {
                    "standard" -> {
                        strID = mModelList[position].StandardID
                        str = mModelList[position].StandardName

                        filterInterface.filterData(filterType)

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
//                    holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.right_white_bg))
//                    holder.img.visibility = View.VISIBLE

//                    for (i in 0 until mModelList.size) {
//                        when (filterType) {
//                            "exam" -> {
//                                AppConstants.FILTER_BOARD_ID =
//                                    AppConstants.FILTER_BOARD_ID + "," + mModelList[position].isSelected
//                            }
//                            "subject" -> {
//                                AppConstants.FILTER_SUBJECT_ID =
//                                    AppConstants.FILTER_SUBJECT_ID + "," + mModelList[position].SubjectID
//                            }
//                            "tutor" -> {
//                                AppConstants.FILTER_TUTOR_ID =
//                                    AppConstants.FILTER_TUTOR_ID + "," + mModelList[position].TutorID
//                            }
//                        }
//                    }

                    filterInterface.filterData(filterType)

                } else {
//                holder.view.setBackgroundColor(Color.WHITE)
//                    holder.img.visibility = View.GONE
//                    holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.white_ring_bg))

                    when (filterType) {
                        "exam" -> {
                            AppConstants.FILTER_BOARD_ID =
                                AppConstants.FILTER_BOARD_ID + "," + mModelList[position].CourseID
                        }
                        "subject" -> {
                            AppConstants.FILTER_SUBJECT_ID =
                                AppConstants.FILTER_SUBJECT_ID + "," + mModelList[position].SubjectID
                        }
                        "tutor" -> {
                            AppConstants.FILTER_TUTOR_ID =
                                AppConstants.FILTER_TUTOR_ID + "," + mModelList[position].TutorID
                        }
                    }

                    filterInterface.filterData(filterType)
                }
                notifyDataSetChanged()
            }
        }

        if (selectionType == "single") {

            if (raw_index == position) {

                holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.right_blue))
                holder.img.setBackgroundResource(R.drawable.gray_ring_bg)
//                holder.img.visibility = View.VISIBLE
                model.isSelected = !model.isSelected
            } else {
//                holder.img.visibility = View.GONE
                holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.white_ring_bg))
                holder.img.setBackgroundResource(R.drawable.gray_ring_bg)
//                model.isSelected = model.isSelected

            }

        } else {
            if (model.isSelected) {

                holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.white_right_icn))
                holder.img.background = cotext.resources.getDrawable(R.drawable.login_btn_bg)

            } else {
                holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.white_ring_bg))
                holder.img.background = cotext.resources.getDrawable(R.drawable.gray_ring_bg)
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

    fun sendArray(): ArrayList<PackageData.PackageDataList> {

        return mModelList!!
    }

    fun sendStandard(): String {
        return strID
    }

}
