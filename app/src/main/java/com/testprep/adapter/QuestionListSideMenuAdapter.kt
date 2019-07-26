package com.testprep.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.interfaces.FilterTypeSelectionInteface
import com.testprep.models.QuestionTypeModel
import java.util.*

class QuestionListSideMenuAdapter(
    var context: Context,
    var listDataHeader: ArrayList<String>,
    var listChildData: ArrayList<QuestionTypeModel>,
    val filterTypeSelectionInteface: FilterTypeSelectionInteface,
    val come_from: String
) : RecyclerView.Adapter<QuestionListSideMenuAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {
        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.question_list_header,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listDataHeader.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.title.text = listDataHeader[p1]
        p0.list.layoutManager = GridLayoutManager(context, 5)

        p0.list.adapter = QuestionChildListAdapter(context, listChildData, filterTypeSelectionInteface, come_from)

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.queList_tvHeader)
        var list: RecyclerView = itemView.findViewById(R.id.queList_tvList)

    }

//    override fun getChild(groupPosition: Int, childPosition: Int): String {
//        return this.listChildData[this.listDataHeader[groupPosition]]!![childPosition]
//    }
//
//    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
//        return childPosition.toLong()
//    }
//
//    override fun getChildView(
//        groupPosition: Int, childPosition: Int,
//        isLastChild: Boolean, convertView: View?, parent: ViewGroup
//    ): View {
//
//        Log.d("child", " in getChildView()")
//
//        var convertView = convertView
//
//        val childText = getChild(groupPosition, childPosition)
//
//        if (convertView == null) {
//            val infalInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            convertView = infalInflater.inflate(R.layout.question_list_child, null)
//        }
//
//        val txtListChild = convertView!!.findViewById(R.id.queList_tvList) as RecyclerView
//
//        txtListChild.layoutManager = GridLayoutManager(context, 5)
//
//        txtListChild.adapter = QuesChildListAdapter(context, )
//
//        txtListChild.text = childText
//        return convertView
//    }
//
//    override fun getChildrenCount(groupPosition: Int): Int {
//        return this.listChildData[this.listDataHeader[groupPosition]]!!.size
//    }
//
//    override fun getGroup(groupPosition: Int): String {
//        return this.listDataHeader[groupPosition]
//    }
//
//    override fun getGroupCount(): Int {
//        return this.listDataHeader.size
//    }
//
//    override fun getGroupId(groupPosition: Int): Long {
//        return groupPosition.toLong()
//    }
//
//    override fun getGroupView(
//        groupPosition: Int, isExpanded: Boolean,
//        convertView: View?, parent: ViewGroup
//    ): View {
//        var convertView = convertView
//        val headerTitle = getGroup(groupPosition)
//        if (convertView == null) {
//            val infalInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            convertView = infalInflater.inflate(R.layout.question_list_header, null)
//        }
//
//        val lblListHeader = convertView!!.findViewById(R.id.queList_tvHeader) as TextView
//        lblListHeader.setTypeface(null, Typeface.BOLD)
//        lblListHeader.text = headerTitle
//
//        return convertView
//    }
//
//    override fun hasStableIds(): Boolean {
//        return false
//    }
//
//    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
//        return true
//    }

}
