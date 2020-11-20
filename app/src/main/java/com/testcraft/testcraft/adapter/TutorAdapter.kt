package com.testcraft.testcraft.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls

class TutorAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
    RecyclerView.Adapter<TutorAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.marketplace_tutor_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {
        Picasso.get()
            .load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon)
            .into(p0.image)

        p0.title.text = dataList[p1].TutorName

        p0.clMain.setOnClickListener {

            CommonWebCalls.callToken(context, "1", "", ActionIdData.C806, ActionIdData.T806)

            AppConstants.isFirst = 15
            val bundle = Bundle()
            bundle.putString("tutor_id", dataList[p1].TutorID)
            setFragments(bundle)

        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.tutor_item_image)
        var title: TextView = itemView.findViewById(R.id.tutor_item_tvName)
        var clMain: ConstraintLayout = itemView.findViewById(R.id.tutor_item_clMain)
    }
}
