package com.testcraft.testcraft.adapter

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
import com.testcraft.testcraft.utils.AppConstants

class MarketPlacePkgAdapter(
    val context: Context,
    val dataList: ArrayList<PackageData.PackageDataList>
) :
    RecyclerView.Adapter<MarketPlacePkgAdapter.viewholder>() {

    var row_index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {
//
//        val view = LayoutInflater.from(p0.context).inflate(R.layout.marketplace_pkg_list_item, p0, false)
//        val width = recyclerView.getWidth()
//        val params = view.layoutParams
//        params.width = (width * 0.8).toInt()
//        view.layoutParams = params
//        return MarketPlacePkgAdapter(view)

        return viewholder(
//            LayoutInflater.from(context).inflate(R.layout.list_item_test_package, p0, false)
            LayoutInflater.from(context).inflate(R.layout.marketplace_pkg_list_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size

    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        if (dataList != null && dataList.size > 0) {
            p0.std.text = dataList[p1].TestPackageName
            p0.sub.text = dataList[p1].SubjectName
            p0.price.text = "Tutor" + dataList[p1].TutorName
            p0.desc.text = dataList[p1].TestPackageDescription

            if (dataList[p1].Icon != null) {
                Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon).into(p0.image)
            }

//        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].TestPackageName.substring(0, 1)))

//            if (dataList[p1].InstituteName != "" && dataList[p1].InstituteName != null) {
//                p0.createdby.text =
//                    Html.fromHtml("created by " + "<font color=\"#3ea7e0\">" + dataList[p1].InstituteName + "</font>")
//            } else {
//                p0.createdby.text =
//                    Html.fromHtml("created by " + "<font color=\"#3ea7e0\">" + dataList[p1].TutorName + "</font>")
//            }
//        p0.stitle.text = dataList[p1].TestPackageName

//            p0.tvBuy.setOnClickListener {

            //                val intent = Intent(context, PackageDetailActivity::class.java)
//                intent.putExtra("pkgid", dataList[p1].TestPackageID)
//                intent.putExtra("pname", dataList[p1].TestPackageName)
//                intent.putExtra("sprice", dataList[p1].TestPackageSalePrice)
//                intent.putExtra("lprice", dataList[p1].TestPackageListPrice)
//                intent.putExtra("desc", dataList[p1].TestPackageDescription)
//                intent.putExtra("test_type_list", dataList[p1].TestType)
//                intent.putExtra("come_from", "selectpackage")
//                intent.putExtra("position", dataList[p1].TestPackageName.substring(0, 1).single())
//                context.startActivity(intent)

//                DialogUtils.createConfirmDialog(
//                    context,
//                    "",
//                    "Are you sure you want to buy this package?",
//                    "Yes",
//                    "No",
//                    DialogInterface.OnClickListener { dialog, which ->
////                        callAddTestPackageApi(dataList[p1].TestPackageID)
//
//                    },
//                    DialogInterface.OnClickListener { dialog, which ->
//                        dialog.dismiss()
//
//
//                    }).show()
//
//            }

            p0.mainll.setOnClickListener {

                AppConstants.isFirst = 14
                val bundle = Bundle()
                bundle.putString("pkgid", dataList[p1].TestPackageID)
                bundle.putString("come_from", "selectpackage")
                DashboardActivity.setFragments(bundle)


//                val intent = Intent(context, PackageDetailActivity::class.java)
//                intent.putExtra("pkgid", dataList[p1].TestPackageID)
//                intent.putExtra("tutor_id", dataList[p1].TutorID)
//                intent.putExtra("come_from", "selectpackage")
//                context.startActivity(intent)
            }
        }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.package_item_ivImage)
//        var title: TextView = itemView.findViewById(R.id.package_name)
//        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
//        var p_select: ImageView = itemView.findViewById(R.id.package_select)

        var std: TextView = itemView.findViewById(R.id.package_item_tvStd)
        var sub: TextView = itemView.findViewById(R.id.package_item_tvSub)
        var price: TextView = itemView.findViewById(R.id.package_item_tvPrice)
        var mainll: ConstraintLayout = itemView.findViewById(R.id.mall)
        //        var tvBuy: ImageView = itemView.findViewById(R.id.package_item_ivCart)
        var desc: TextView = itemView.findViewById(R.id.package_item_tvDesc)
    }

}

