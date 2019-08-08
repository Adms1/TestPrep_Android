package com.testprep.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testprep.R
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.PackageData
import com.testprep.utils.AppConstants

class FilterListAdapter(
    val context: Context,
    val dataList: ArrayList<PackageData.PackageDataList>,
    var filterTypeSelectionInteface: FilterTypeSelectionInteface
) :
    RecyclerView.Adapter<FilterListAdapter.viewholder>() {

    var row_index = 0

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.filter_type_list_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {
        p0.title.text = dataList[p1].TestPackageName

        when (dataList[p1].TestPackageName) {
            "Boards" -> {
                if (AppConstants.FILTER_BOARD_ID != "") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
            "Standard" -> {

                if (AppConstants.FILTER_STANDARD_ID != "") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
            "Competitive Exams" -> {
                if (AppConstants.FILTER_BOARD_ID != "" && AppConstants.FILTER_BOARD_ID != "111") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
            "Subjects" -> {
                if (AppConstants.FILTER_SUBJECT_ID != "") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
            "Tutor" -> {
                if (AppConstants.FILTER_TUTOR_ID != "") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
        }

        p0.title.setOnClickListener {
            filterTypeSelectionInteface.getType(p1)

            row_index = p1
            notifyDataSetChanged()
        }

        if (row_index == p1) {
            p0.title.setTextColor(context.resources.getColor(R.color.white))
            p0.mainll.setBackgroundColor(context.resources.getColor(R.color.colorPrimaryDark))

            p0.select.setImageResource(R.drawable.white_ring_bg)

        } else {
            p0.title.setTextColor(context.resources.getColor(R.color.gray))
            p0.mainll.setBackgroundColor(context.resources.getColor(R.color.light_blue))

            p0.select.setImageResource(R.drawable.blue_round)

        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.filter_item_tvType)
        var select: ImageView = itemView.findViewById(R.id.filter_item_ivSelect)
        var mainll: ConstraintLayout = itemView.findViewById(R.id.filter_item_mainll)
    }
}
