package com.testcraft.testcraft.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls

@SuppressLint("SetTextI18n")
class TutorPackageAdapter(
    val context: Context,
    val dataList: ArrayList<PackageData.PackageDataList>
) :
    RecyclerView.Adapter<TutorPackageAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_tutors_pkgs, p0, false)
//            LayoutInflater.from(context).inflate(R.layout.new_prefrence_item_layout, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {
//        if (dataList[p1].Icon == null) {
//
//            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
//            p0.stitle.text = "M"
//            p0.p_select.visibility = View.VISIBLE
//
//        } else {
        Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon).into(p0.image)
//
//        }

//        p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
        p0.title.text = dataList[p1].TestPackageName
//        p0.stitle.text = dataList[p1].TestPackageName.substring(0, 1)
        p0.sub.text = dataList[p1].SubjectName
        p0.price.text = "₹" + dataList[p1].TestPackageSalePrice

        p0.clMain.setOnClickListener {

            CommonWebCalls.callToken(context, "1", "", ActionIdData.C2802, ActionIdData.T2802)

            AppConstants.isFirst = 14
            val bundle = Bundle()
            bundle.putString("pkgid", dataList[p1].TestPackageID)
            bundle.putString("come_from", "mypackage")
            DashboardActivity.setFragments(bundle)


//            val intent = Intent(context, PackageDetailActivity::class.java)
//            intent.putExtra("pkgid", dataList[p1].TestPackageID)
//            intent.putExtra("tutor_id", dataList[p1].TutorID)
//            intent.putExtra("come_from", "selectpackage")
//            context.startActivity(intent)

        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.item_tutor_package_image)
        var title: TextView = itemView.findViewById(R.id.item_tutor_package_name)
        //        var stitle: TextView = itemView.findViewById(R.id.item_tutor_package_name_short)
        var price: TextView = itemView.findViewById(R.id.item_tutor_package_price)
        var next: ImageView = itemView.findViewById(R.id.item_tutor_package_btnNext)
        var sub: TextView = itemView.findViewById(R.id.item_tutor_package_subject)
        var clMain: ConstraintLayout = itemView.findViewById(R.id.item_tutor_package_main)
    }
}

