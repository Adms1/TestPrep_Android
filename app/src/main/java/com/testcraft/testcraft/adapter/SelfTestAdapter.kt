package com.testcraft.testcraft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.models.GetSelfTest
import com.testcraft.testcraft.utils.AppConstants

class SelfTestAdapter(val context: Context, var datalist: ArrayList<GetSelfTest.GetSelfData>) : RecyclerView.Adapter<SelfTestAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(LayoutInflater.from(context).inflate(R.layout.mytest_item_layout, p0, false))
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        Picasso.get().load(AppConstants.IMAGE_BASE_URL + datalist[p1].Icon).into(p0.image)
        p0.name.text = datalist[p1].TestPackageName

        if (datalist[p1].NumberOfComletedTest == datalist[p1].NumberOfTest) {
            p0.ivComplete.visibility = View.VISIBLE
        } else {
            p0.ivComplete.visibility = View.GONE
        }

        if (datalist[p1].NumberOfTest == "1") {

            p0.test.text =
                datalist[p1].NumberOfComletedTest + "/" + datalist[p1].NumberOfTest + " Test"

        } else {

            p0.test.text =
                datalist[p1].NumberOfComletedTest + "/" + datalist[p1].NumberOfTest + " Tests"

        }

        p0.mainll.setOnClickListener {

                AppConstants.PKG_ID = datalist[p1].StudentTestPackageID.toString()
                AppConstants.PKG_NAME = datalist[p1].TestPackageName
            AppConstants.IS_SELF_TEST = "true"

                AppConstants.isFirst = 12

                DashboardActivity.setFragments(null)

        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivComplete: ImageView = itemView.findViewById(R.id.mytest_item_select)

        var name: TextView = itemView.findViewById(R.id.mytest_item_name)

        var mainll: ConstraintLayout = itemView.findViewById(R.id.mytest_item_main)
        var test: TextView = itemView.findViewById(R.id.mytest_item_test)
        var image: ImageView = itemView.findViewById(R.id.mytest_item_image)

    }
}
