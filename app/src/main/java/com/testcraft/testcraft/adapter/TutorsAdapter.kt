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
import com.testcraft.testcraft.activity.DashboardActivity
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls

class TutorsAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
    RecyclerView.Adapter<TutorsAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_tutors, p0, false)
//            LayoutInflater.from(context).inflate(R.layout.new_prefrence_item_layout, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        if (p1 == dataList.size - 1) {
            p0.ivLine.visibility = View.GONE
        } else {
            p0.ivLine.visibility = View.VISIBLE
        }
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
        p0.title.text = dataList[p1].TutorName
//        p0.stitle.text = dataList[p1].TutorName.substring(0, 1)
        p0.price.text = dataList[p1].TutorEmail
        p0.std.text = ""

        p0.clMain.setOnClickListener {

            CommonWebCalls.callToken(context, "1", "", ActionIdData.C2701, ActionIdData.T2701)

            AppConstants.isFirst = 15
            val bundle = Bundle()
            bundle.putString("tutor_id", dataList[p1].TutorID)
            DashboardActivity.setFragments(bundle)

//            val intent = Intent(context, TutorProfileFragment::class.java)
//            intent.putExtra("tutor_id", dataList[p1].TutorID)
//            context.startActivity(intent)

//            val intent = Intent(context, PackageDetailActivity::class.java)
//            intent.putExtra("pkgid", dataList[p1].TestPackageID)
//            intent.putExtra("pname", dataList[p1].TestPackageName)
//            intent.putExtra("sprice", dataList[p1].TestPackageSalePrice)
//            intent.putExtra("lprice", dataList[p1].TestPackageListPrice)
//            intent.putExtra("desc", dataList[p1].TestPackageDescription)
//            intent.putExtra("test_type_list", dataList[p1].TestType)
//            if (dataList[p1].InstituteName != "" && dataList[p1].InstituteName != null) {
//                intent.putExtra("created_by", dataList[p1].InstituteName)
//            } else {
//                intent.putExtra("created_by", dataList[p1].TutorName)
//            }
//            intent.putExtra("tutor_id", dataList[p1].TutorID)
//            intent.putExtra("come_from", "selectpackage")
//            intent.putExtra("position", dataList[p1].TestPackageName.substring(0, 1).single())
//            context.startActivity(intent)

        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.tutor_image)
        var title: TextView = itemView.findViewById(R.id.tutor_item_tvName)
        //        var stitle: TextView = itemView.findViewById(R.id.tutor_name_short)
        var price: TextView = itemView.findViewById(R.id.tutor_item_tvPrice)
        var next: ImageView = itemView.findViewById(R.id.tutor_item_btnNext)
        var std: TextView = itemView.findViewById(R.id.tutor_item_tvStd)
        var clMain: ConstraintLayout = itemView.findViewById(R.id.tutor_clMain)
        var ivLine: View = itemView.findViewById(R.id.tutor_item_line)
    }
}


