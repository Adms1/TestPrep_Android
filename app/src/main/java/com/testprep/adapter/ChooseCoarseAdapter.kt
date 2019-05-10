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
import com.testprep.R
import com.testprep.activity.SelectCourseTypeActivity
import com.testprep.models.GetCourseListData
import com.testprep.utils.AppConstants
import com.testprep.utils.Utils

class ChooseCoarseAdapter(val context: Context, val type: String, val dataList: ArrayList<GetCourseListData>) :
    RecyclerView.Adapter<ChooseCoarseAdapter.viewholder>() {

    var row_index = -1

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

        if (type == "main_course") {
            p0.title.text = dataList[p1].CourseTypeName

            p0.title.setOnClickListener {


                //                Utils.saveArrayList(context, AppConstants.COURSE_FLOW_ARRAY, AppConstants.COURSE_FLOW)

                row_index = p1
                notifyDataSetChanged()

//                var fragment: Fragment = CoarseTypeFragment()
//                var bundle = Bundle()
//                bundle.putString("course_type", dataList[p1].CourseTypeID.toString())
//                fragment.arguments = bundle
//                (context as DashboardActivity).supportFragmentManager.beginTransaction()
//                    .add(R.id.container, fragment).addToBackStack(null).commit()

                Log.d("flow1", Utils.getStringValue(context, AppConstants.COURSE_FLOW, ""))

                val mIntent = Intent(context, SelectCourseTypeActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("course_type", dataList[p1].CourseTypeID.toString())
                mIntent.putExtras(mBundle)
                context.startActivity(mIntent)

                AppConstants.COURSE_FLOW_ARRAY.add(dataList[p1].CourseTypeName)

            }


        } else if (type == "course_type") {
            p0.title.text = dataList[p1].CourseName

            p0.title.setOnClickListener {

                row_index = p1
                notifyDataSetChanged()

                val mIntent = Intent(context, SelectCourseTypeActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("course_type", dataList[p1].CourseTypeID.toString())
                mIntent.putExtras(mBundle)
                context.startActivity(mIntent)

                AppConstants.COURSE_FLOW_ARRAY.add(dataList[p1].CourseName)

//                AppConstants.COURSE_FLOW_ARRAY.add(dataList[p1].CourseName)
//                Utils.saveArrayList(context, AppConstants.COURSE_FLOW_ARRAY, AppConstants.COURSE_FLOW)

//                Utils.setStringValue(context, AppConstants.COURSE_FLOW, AppConstants.COURSE_FLOW + ">" + dataList[p1].CourseName)

            }
        }

        if (row_index == p1) {
            p0.title.setBackgroundResource(R.drawable.dark_blue_gredient_bg)
        } else {
            p0.title.setBackgroundResource(R.drawable.blue_gradient_bg)
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.drawer_list_title)

    }
}

