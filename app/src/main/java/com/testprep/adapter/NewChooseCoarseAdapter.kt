package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.activity.NewActivity
import com.testprep.activity.PrefrenceActivity
import com.testprep.models.PackageData
import com.testprep.utils.AppConstants
import com.testprep.utils.Utils

class NewChooseCoarseAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
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

        p0.image.setOnClickListener {

            AppConstants.FILTER_COURSE_TYPE_ID = dataList[p1].CourseTypeID.toString()
            Utils.setStringValue(context, AppConstants.COURSE_TYPE_ID, dataList[p1].CourseTypeID.toString())
            Utils.setStringValue(context, "course_type_name", dataList[p1].CourseTypeName)

            val intent = Intent(context, PrefrenceActivity::class.java)
            intent.putExtra("examtype", dataList[p1].CourseTypeID.toString())
            context.startActivity(intent)
            (context as NewActivity).finish()
            notifyDataSetChanged()
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.package_image)
        var title: TextView = itemView.findViewById(R.id.package_name)
        var stitle: TextView = itemView.findViewById(R.id.package_name_short)

    }
}

