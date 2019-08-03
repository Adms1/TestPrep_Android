package com.testprep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.testprep.R
import com.testprep.models.PackageData
import java.util.*

class CoverFlowAdapter(private val mContext: Context) : BaseAdapter() {

    private var mData = ArrayList<PackageData.PackageDataList>(0)

    fun setData(data: ArrayList<PackageData.PackageDataList>) {
        mData = data
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(pos: Int): Any {
        return mData.get(pos)
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View? {

        var rowView: View? = convertView

        if (rowView == null) {
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.item_coverflow, null)

            val viewHolder = ViewHolder()
            viewHolder.text = rowView!!.findViewById<View>(R.id.label) as TextView
            viewHolder.image = rowView.findViewById<View>(R.id.image) as ImageView
            rowView.tag = viewHolder
        }

        val holder = rowView.tag as ViewHolder

        holder.image!!.setImageResource(R.drawable.pp_1)
        holder.text!!.text = mData[position].TestPackageName

        return rowView
    }


    internal class ViewHolder {
        var text: TextView? = null
        var image: ImageView? = null
    }
}
