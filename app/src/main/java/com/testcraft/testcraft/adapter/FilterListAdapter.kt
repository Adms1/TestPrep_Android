package com.testcraft.testcraft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.interfaces.FilterTypeSelectionInteface
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.AppConstants

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
                if (AppConstants.FILTER_BOARD_ID != "" && AppConstants.FILTER_BOARD_ID != "0") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
            "Standard" -> {

                if (AppConstants.FILTER_STANDARD_ID != "" && AppConstants.FILTER_STANDARD_ID != "0") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
            "Course" -> {
                if (AppConstants.FILTER_BOARD_ID != "" && AppConstants.FILTER_BOARD_ID != "0") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
            "Subjects" -> {
                if (AppConstants.FILTER_SUBJECT_ID != "" && AppConstants.FILTER_SUBJECT_ID != "0") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
            "Tutor" -> {
                if (AppConstants.FILTER_TUTOR_ID != "" && AppConstants.FILTER_TUTOR_ID != "0") {
                    p0.select.visibility = View.VISIBLE
                } else {
                    p0.select.visibility = View.INVISIBLE
                }
            }
            "Price" -> {

                if (AppConstants.FILTER_FROM_PRICE == "0" && AppConstants.FILTER_TO_PRICE == "5000") {
                    p0.select.visibility = View.INVISIBLE
                }

//                if (AppConstants.FILTER_TO_PRICE.toInt() != 5000 || AppConstants.FILTER_FROM_PRICE.toInt() != 0) {
//                    p0.select.visibility = View.VISIBLE
//                }
                else {
                    p0.select.visibility = View.VISIBLE
                }

            }
        }

        p0.title.setOnClickListener {
            filterTypeSelectionInteface.getType("adapter", 0, p1)

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
