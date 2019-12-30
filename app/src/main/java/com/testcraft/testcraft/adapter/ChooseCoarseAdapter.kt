package adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.SelectBoardActivity
import com.testcraft.testcraft.activity.SelectStandardActivity
import com.testcraft.testcraft.activity.SelectSubjectActivity
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.Utils

class ChooseCoarseAdapter(
    val context: Context,
    val type: String,
    val dataList: ArrayList<PackageData.PackageDataList>,
    val selectType: String
) : RecyclerView.Adapter<ChooseCoarseAdapter.viewholder>() {

    var row_index = -1
    var ismulti = -1
    var selected: ArrayList<Int> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_drawer_menu,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        when (type) {

            "main_course" -> {

                p0.title.text = dataList[p1].CourseTypeName

                p0.title.setOnClickListener {

                    Utils.setStringValue(
                        context,
                        AppConstants.COURSE_TYPE_ID,
                        dataList[p1].CourseTypeID.toString()
                    )

                    ismulti = 0

                    row_index = p1
                    notifyDataSetChanged()

                    Log.d("flow1", Utils.getStringValue(context, AppConstants.COURSE_FLOW, ""))

                    val mIntent = Intent(context, SelectBoardActivity::class.java)
                    val mBundle = Bundle()
                    mBundle.putString("course_type", dataList[p1].CourseTypeID.toString())
                    mBundle.putString("course_name", dataList[p1].CourseTypeName)
                    mIntent.putExtras(mBundle)
                    context.startActivity(mIntent)
//                    (context as DashboardActivity).overridePendingTransition(
//                        R.anim.slide_in_right,
//                        R.anim.slide_out_left
//                    )

                    AppConstants.COURSE_FLOW_ARRAY.add(dataList[p1].CourseTypeName)

                }
            }

            "course_type" -> {

                p0.title.text = dataList[p1].CourseName

                p0.title.setOnClickListener {

                    Utils.setStringValue(
                        context,
                        AppConstants.COURSE_ID,
                        dataList[p1].CourseID.toString()
                    )

                    ismulti = 0
                    row_index = p1
                    notifyDataSetChanged()

                    if (Utils.getStringValue(context, AppConstants.COURSE_TYPE_ID, "") != "1") {
                        val mIntent = Intent(context, SelectSubjectActivity::class.java)
                        val mBundle = Bundle()
                        mBundle.putString("standard_id", dataList[p1].CourseID.toString())
                        mIntent.putExtras(mBundle)
                        context.startActivity(mIntent)
//                        (context as SelectBoardActivity).overridePendingTransition(
//                            R.anim.slide_in_right,
//                            R.anim.slide_out_left
//                        )

                    } else {
                        val mIntent = Intent(context, SelectStandardActivity::class.java)
                        val mBundle = Bundle()
                        mBundle.putString("course_id", dataList[p1].CourseID.toString())
                        mIntent.putExtras(mBundle)
                        context.startActivity(mIntent)
//                        (context as SelectBoardActivity).overridePendingTransition(
//                            R.anim.slide_in_right,
//                            R.anim.slide_out_left
//                        )
                    }

                    AppConstants.COURSE_FLOW_ARRAY.add(dataList[p1].CourseName)

                }
            }

            "course_subject" -> {

                p0.title.text = dataList[p1].SubjectName

                p0.title.setOnClickListener {

                    ismulti = -1
                    row_index = p1
                    notifyDataSetChanged()

//                    dataList[p1].isSelected = (!dataList[p1].isSelected)
//                    p0.title.setBackgroundResource(if (dataList[p1].isSelected) R.drawable.dark_blue_gredient_bg else R.drawable.blue_gradient_bg)
                }
            }

            "course_standard" -> {
//                row_index = p1
//                notifyDataSetChanged()

                p0.title.text = dataList[p1].StandardName

                p0.title.setOnClickListener {

                    Utils.setStringValue(
                        context,
                        AppConstants.STANDARD_ID,
                        dataList[p1].StandardID.toString()
                    )

                    ismulti = 0
                    row_index = p1
                    notifyDataSetChanged()

                    val mIntent = Intent(context, SelectSubjectActivity::class.java)
                    val mBundle = Bundle()
                    mBundle.putString("standard_id", dataList[p1].StandardID.toString())
                    mIntent.putExtras(mBundle)
                    context.startActivity(mIntent)
//                    (context as SelectStandardActivity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                    AppConstants.COURSE_FLOW_ARRAY.add(dataList[p1].StandardName)

                }
            }

            "course_stdSubject" -> {
                p0.title.text = dataList[p1].SubjectName

                p0.title.setOnClickListener {

                    //                    p0.title.setBackgroundResource(R.drawable.dark_blue_gredient_bg)

                    ismulti = -1
                    row_index = p1
                    notifyDataSetChanged()

//                    val mIntent = Intent(context, SelectPackageActivity::class.java)
//                    mIntent.putExtra("subject_id", dataList[p1].SubjectID.toString())
//                    context.startActivity(mIntent)

//                    AppConstants.COURSE_FLOW_ARRAY.add(dataList[p1].SubjectName)

                }

            }
        }

        if (selectType == "yes") {
            p0.title.setBackgroundResource(R.drawable.dark_blue_gredient_bg)

        } else {
            if (ismulti == 0) {

                if (row_index == p1) {
                    p0.title.setBackgroundResource(R.drawable.dark_blue_gredient_bg)
                } else {
                    p0.title.setBackgroundResource(R.drawable.blue_gradient_bg)
                }

            } else if (ismulti == -1) {

                if (row_index == p1) {
                    p0.title.setBackgroundResource(R.drawable.dark_blue_gredient_bg)

                    if (selected.size > 0) {

                        for (i in 0..selected.size) {
                            if (selected.contains(dataList[p1].SubjectID.toInt())) {
                                selected.remove(dataList[p1].SubjectID.toInt())
                                p0.title.setBackgroundResource(R.drawable.blue_gradient_bg)
                                break

                            } else {
                                selected.add(dataList[p1].SubjectID.toInt())
//                            p0.title.setBackgroundResource(R.drawable.dark_blue_gredient_bg)

                                break

                            }
                        }

                        Log.d("selected array : ", " $selected")
                    } else {
                        selected.add(dataList[p1].SubjectID.toInt())
                    }
                }
            }
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.drawer_list_title)

    }

    fun getIds(): String {

        return selected.toString().replace("[", "").replace("]", "")

    }
}

