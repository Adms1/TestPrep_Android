package com.testcraft.testcraft.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.NewActivity
import com.testcraft.testcraft.activity.PrefrenceActivity
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.Utils

class NewChooseCoarseAdapter(
    val context: Context,
    val dataList: ArrayList<PackageData.PackageDataList>, val come: String
) :
    RecyclerView.Adapter<NewChooseCoarseAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.new_choose_coarse_item_layout,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].CourseTypeName.substring(0, 1)))

//        p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.select_exam_bg))
//        p0.image.setImageDrawable(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon)

        Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon).into(p0.image)

        p0.title.text = dataList[p1].CourseTypeName.replace(" ", "\n")
//        p0.stitle.text = p0.title.text.substring(0, 1)

        p0.mainll.setOnClickListener {

            AppConstants.FILTER_COURSE_TYPE_ID = dataList[p1].CourseTypeID.toString()

            Utils.setStringValue(
                context,
                AppConstants.COURSE_TYPE_ID,
                dataList[p1].CourseTypeID.toString()
            )
            Utils.setStringValue(context, "course_type_name", dataList[p1].CourseTypeName)

            if (dataList[p1].CourseTypeID != 1) {

                CommonWebCalls.callToken(context, "1", "", ActionIdData.C602, ActionIdData.T602)

                Utils.setStringValue(context, AppConstants.STANDARD_ID, "")
                Utils.setStringValue(context, AppConstants.SUBJECT_ID, "")
            } else {

                CommonWebCalls.callToken(context, "1", "", ActionIdData.C601, ActionIdData.T601)
            }

            val intent = Intent(context, PrefrenceActivity::class.java)
            intent.putExtra("examtype", dataList[p1].CourseTypeID.toString())
            intent.putExtra("comefrom", come)
            context.startActivity(intent)
            (context as NewActivity).finish()
            notifyDataSetChanged()
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.package_image)
        var title: TextView = itemView.findViewById(R.id.package_name)
        var mainll: ConstraintLayout = itemView.findViewById(R.id.chose_course_mainll)

    }
}

