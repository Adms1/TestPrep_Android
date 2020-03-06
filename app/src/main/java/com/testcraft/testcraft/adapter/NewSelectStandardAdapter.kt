package com.testcraft.testcraft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.PrefrenceActivity
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls
import com.testcraft.testcraft.utils.Utils

class NewSelectStandardAdapter(
    val context: Context,
    val dataList: ArrayList<PackageData.PackageDataList>
) :
    RecyclerView.Adapter<NewSelectStandardAdapter.viewholder>() {

    var row_index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.new_prefrence_item_layout,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].StandardName.substring(0, 1)))

        if (dataList[p1].Icon != "") {
            Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon).into(p0.image)
        } else {
            if (dataList[p1].StandardName.contains(" ")) {
                p0.stitle.text = dataList[p1].StandardName.substring(0, 2)
            } else {
                p0.stitle.text = dataList[p1].StandardName
            }

        }

        p0.title.text = dataList[p1].StandardName

        p0.image.setOnClickListener {

            CommonWebCalls.callToken(context, "1", "", ActionIdData.C702, ActionIdData.T702)

            p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))

            Utils.setStringValue(
                context,
                AppConstants.STANDARD_ID,
                dataList[p1].StandardID
            )
            Utils.setStringValue(context, "standard_name", dataList[p1].StandardName)

            row_index = p1
            notifyDataSetChanged()

            (context as PrefrenceActivity).callSubjectList(dataList[p1].StandardID)
            notifyDataSetChanged()
        }

        if (dataList[p1].Icon == "") {
            if (row_index == p1) {
                p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))
                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
                p0.p_select.visibility = View.VISIBLE
            } else {
                p0.title.setTextColor(context.resources.getColor(R.color.black))
                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
                p0.p_select.visibility = View.GONE
            }
        } else {
            if (row_index == p1) {
                p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))
//                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
                p0.p_select.visibility = View.VISIBLE
            } else {
                p0.title.setTextColor(context.resources.getColor(R.color.black))
//                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
                p0.p_select.visibility = View.GONE
            }
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.package_image)
        var title: TextView = itemView.findViewById(R.id.package_name)
        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
        var p_select: ImageView = itemView.findViewById(R.id.package_select)
    }
}
