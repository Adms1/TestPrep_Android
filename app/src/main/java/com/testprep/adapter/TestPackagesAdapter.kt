package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.activity.PackageDetailActivity
import com.testprep.models.PackageData

class TestPackagesAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
    RecyclerView.Adapter<TestPackagesAdapter.viewholder>() {

    var row_index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_test_package, p0, false)
//            LayoutInflater.from(context).inflate(R.layout.new_prefrence_item_layout, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return if (dataList != null && dataList.size > 0) {
            dataList.size
        } else {
            5
        }
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        if (dataList != null && dataList.size > 0) {
            p0.std.text = dataList[p1].TestPackageName
            p0.sub.text = "Mathematics"
            p0.price.text = "₹" + dataList[p1].TestPackageSalePrice

//        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].TestPackageName.substring(0, 1)))
            p0.createdby.text = Html.fromHtml("created by " + "<font color=\"#3ea7e0\">" + "TestPrep" + "</font>")
//        p0.stitle.text = dataList[p1].TestPackageName

            p0.mainll.setOnClickListener {

                val intent = Intent(context, PackageDetailActivity::class.java)
                intent.putExtra("pkgid", dataList[p1].TestPackageID)
                intent.putExtra("pname", dataList[p1].TestPackageName)
                intent.putExtra("sprice", dataList[p1].TestPackageSalePrice)
                intent.putExtra("lprice", dataList[p1].TestPackageListPrice)
                intent.putExtra("desc", dataList[p1].TestPackageDescription)
                intent.putExtra("test_type_list", dataList[p1].TestType)
                intent.putExtra("come_from", "selectpackage")
                intent.putExtra("position", dataList[p1].TestPackageName.substring(0, 1).single())
                context.startActivity(intent)
            }
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        var image: ImageView = itemView.findViewById(R.id.package_image)
//        var title: TextView = itemView.findViewById(R.id.package_name)
//        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
//        var p_select: ImageView = itemView.findViewById(R.id.package_select)

        var std: TextView = itemView.findViewById(R.id.testpkg_item_tvStd)
        var sub: TextView = itemView.findViewById(R.id.testpkg_item_tvSub)
        var price: TextView = itemView.findViewById(R.id.testpkg_item_tvPrice)
        var mainll: ConstraintLayout = itemView.findViewById(R.id.mainll)
        var addcart: TextView = itemView.findViewById(R.id.testpkg_item_btnAddCart)
        var createdby: TextView = itemView.findViewById(R.id.testpkg_item_tvCreated)
    }
}

