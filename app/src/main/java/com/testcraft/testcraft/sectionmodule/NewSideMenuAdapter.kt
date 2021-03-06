package com.testcraft.testcraft.sectionmodule

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.interfaces.FilterTypeSelectionInteface
import com.testcraft.testcraft.models.QuestionTypeModel
import com.testcraft.testcraft.sectionmodule.NewTabQuestionActivity.Companion.curr_index
import com.testcraft.testcraft.sectionmodule.NewTabQuestionActivity.Companion.q_grppos1
import com.testcraft.testcraft.utils.AppConstants
import java.util.*

class NewSideMenuAdapter(
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
        val ivInfo = vieww.findViewById(R.id.queList_ivInfo) as ImageView

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
        val answer = getChild(groupPosition, childPosition)

        var vieww: View? = null

        val infalInflater =
            this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        vieww = infalInflater.inflate(R.layout.question_list_child, null)

        val textViewChild = vieww!!.findViewById(R.id.queList_tvChild) as RecyclerView

//        textViewChild.setText(answer.toString())

        textViewChild.layoutManager = GridLayoutManager(context, 5)

//        val gv = convertView as GridView
        textViewChild.adapter = ImageViewAdapter(
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

var old_index = 0
var old_grp = 0

class ImageViewAdapter(
    private val context: Context,
    var grpPos: Int,
    var finalArr: HashMap<String, ArrayList<QuestionTypeModel>>,
    var header: ArrayList<String>,
    var filterTypeSelectionInteface: FilterTypeSelectionInteface,
    private val dataList: ArrayList<QuestionTypeModel>,
    var come_from1: String
) : RecyclerView.Adapter<ImageViewAdapter.Companion.viewholder>() {

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

            pgNum = dataList[p1].page_number

            old_index = curr_index
            old_grp = q_grppos1

            q_grppos1 = grpPos

            for (i in 0..finalArr[header[grpPos]]!!.size) {
                finalArr[header[grpPos]]!![p1].isTrue = finalArr[header[grpPos]]!![p1].qnumber == p1
            }

//            if (come_from1 == "question") {
//                dataList[old_que!!].type = 3
//                finalArr[header[grpPos]]!![p1].type = 1

//            AppConstants.QUE_NUMBER = p1
            curr_index = p1
//            } else {
//
//                AppConstants.QUE_NUMBER1 = p1
//            }

//            old_type = dataList[old_que!!].type

//            p0.childheader.setTextColor(context.resources.getColor(R.color.white))
//            p0.childheader.setBackgroundResource(R.drawable.red_round)

            notifyDataSetChanged()

            NewTabQuestionActivity.sideList!!.setAdapter(
                NewSideMenuAdapter(
                    context,
                    header,
                    finalArr,
                    filterTypeSelectionInteface,
                    "question"
                )
            )

            Log.d("adapter_que_number", "($grpPos)($curr_index)")

            Log.d("itype", "adapter")

            filterTypeSelectionInteface.getType("adapter", grpPos, p1)
//            filterTypeSelectionInteface.getType("adapter", old_grp, old_index)

        }

        when (finalArr[header[grpPos]]!![p1].type) {
            1 -> {

                p0.childheader.setTextColor(NewTabQuestionActivity.context!!.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.blue_round)
            }
            2 -> {
                p0.childheader.setTextColor(NewTabQuestionActivity.context!!.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.green_round)
            }
            3 -> {
                p0.childheader.setTextColor(NewTabQuestionActivity.context!!.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.red_round)
            }
            4 -> {
                p0.childheader.setTextColor(NewTabQuestionActivity.context!!.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.pink_round)
            }
            5 -> {
                p0.childheader.setTextColor(NewTabQuestionActivity.context!!.resources.getColor(R.color.gray))
                p0.childheader.setBackgroundResource(R.drawable.light_gray_round_bg)
            }
        }

        if (come_from1 == "question") {
            if (q_grppos1 == grpPos && curr_index == p1) {

                p0.childheader.setTextColor(context.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.blue_round)
            }
        } else {
            if (AppConstants.QUE_NUMBER1 == p1) {
                p0.childheader.setTextColor(context.resources.getColor(R.color.white))
                p0.childheader.setBackgroundResource(R.drawable.blue_round)
            }
        }

    }

    companion object {

        var pgNum = 0

        class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var childheader: TextView = itemView.findViewById(R.id.queList_tempvChild)
        }

        fun getPageNumber(): Int {

            return pgNum

        }

    }

}


