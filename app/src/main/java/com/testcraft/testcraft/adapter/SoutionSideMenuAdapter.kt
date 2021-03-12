package com.testcraft.testcraft.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.fragments.ViewSolutionFragment.Companion.curr_index1
import com.testcraft.testcraft.fragments.ViewSolutionFragment.Companion.sideList
import com.testcraft.testcraft.fragments.ViewSolutionFragment.Companion.solution_grppos1
import com.testcraft.testcraft.interfaces.FilterTypeSelectionInteface
import com.testcraft.testcraft.models.QuestionTypeModel
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.DialogUtils
import java.util.*

class SoutionSideMenuAdapter(
    val context: Context,
    var header: ArrayList<String>,
    var finalArr: HashMap<String, ArrayList<QuestionTypeModel>>,
    val filterTypeSelectionInteface: FilterTypeSelectionInteface,
    val come_from: String
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return header.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return header[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return 1
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val groupTitle = getGroup(groupPosition) as String

        var vieww: View? = null

        if (vieww == null) {
            val infalInflater = this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            vieww = infalInflater.inflate(R.layout.question_list_header, null)
        }

        val textViewGroup = vieww!!.findViewById(R.id.queList_tvHeader) as TextView
        val infoGroup = vieww.findViewById(R.id.queList_ivInfo) as ImageView

//        if()

        textViewGroup.text = groupTitle
        val mExpandableListView = parent as ExpandableListView
        mExpandableListView.divider = context.resources.getDrawable(R.color.white)
        mExpandableListView.expandGroup(groupPosition)

        return vieww
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {

        var vieww: View? = null

        val infalInflater =
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        vieww = infalInflater.inflate(R.layout.question_list_child, null)


        val textViewChild = vieww!!.findViewById(R.id.queList_tvChild) as RecyclerView

        textViewChild.layoutManager = GridLayoutManager(context, 5)

        textViewChild.adapter = ImageViewAdapter1(
            context,
            groupPosition,
            finalArr,
            header,
            filterTypeSelectionInteface,
            finalArr[header[groupPosition]]!!,
            come_from
        ) //Changed

        return vieww
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}

internal class ImageViewAdapter1(
    private val context: Context,
    var grpPos: Int,
    var finalArr: HashMap<String, ArrayList<QuestionTypeModel>>,
    var header: ArrayList<String>,
    var filterTypeSelectionInteface: FilterTypeSelectionInteface,
    private val dataList: ArrayList<QuestionTypeModel>,
    var come_from1: String
) : RecyclerView.Adapter<ImageViewAdapter1.Companion.viewholder>() {

    var row_index = 1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {
        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.temp_chld_list,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        p0.childheader.text = (p1 + 1).toString()
        p0.childheader.text = dataList[p1].page_number.toString()

        p0.childheader.setOnClickListener {

            pgNum1 = dataList[p1].page_number

            solution_grppos1 = grpPos

            AppConstants.QUE_NUMBER1 = p1
            curr_index1 = p1

            for (i in 0..finalArr[header[grpPos]]!!.size) {
                finalArr[header[grpPos]]!![p1].isTrue = finalArr[header[grpPos]]!![p1].qnumber == p1
            }

            notifyDataSetChanged()

            sideList!!.setAdapter(
                SoutionSideMenuAdapter(
                    context,
                    header,
                    finalArr,
                    filterTypeSelectionInteface,
                    "solution"
                )
            )

            Log.d("que_number", "" + AppConstants.QUE_NUMBER1)

            if (!DialogUtils.isNetworkConnected(context)) {

                val netdialog = Dialog(context)
                netdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                netdialog.setContentView(R.layout.dialog_network)
                netdialog.setCanceledOnTouchOutside(false)

                val btnRetry: TextView = netdialog.findViewById(R.id.network_btnRetry)

                btnRetry.setOnClickListener {
                    if (DialogUtils.isNetworkConnected(context)) {
                        netdialog.dismiss()
                        filterTypeSelectionInteface.getType("adapter", grpPos, p1)
                    }
                }

                netdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                netdialog.setCanceledOnTouchOutside(false)
                netdialog.setCancelable(false)
                netdialog.show()

            } else {
                filterTypeSelectionInteface.getType("adapter", grpPos, p1)
            }

        }

        when {
            finalArr[header[grpPos]]!![p1].type == 1 -> {

                p0.childheader.setTextColor(context.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.blue_round)
            }
            finalArr[header[grpPos]]!![p1].type == 2 -> {
                p0.childheader.setTextColor(context.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.green_round)
            }
            finalArr[header[grpPos]]!![p1].type == 3 -> {
                p0.childheader.setTextColor(context.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.red_round)
            }
            finalArr[header[grpPos]]!![p1].type == 4 -> {
                p0.childheader.setTextColor(context.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.pink_round)
            }
            finalArr[header[grpPos]]!![p1].type == 5 -> {
                p0.childheader.setTextColor(context.resources.getColor(R.color.gray))
                p0.childheader.setBackgroundResource(R.drawable.light_gray_round_bg)
            }
        }

        if (solution_grppos1 == grpPos && curr_index1 == p1) {

            p0.childheader.setTextColor(context.resources.getColor(R.color.white))
            p0.childheader.setBackgroundResource(R.drawable.blue_round)
        }

    }

    companion object {

        var pgNum1 = 0

        class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var childheader: TextView = itemView.findViewById(R.id.queList_tempvChild)
        }


        fun SolutionPageNumber(): Int {

            return pgNum1

        }

    }
}

