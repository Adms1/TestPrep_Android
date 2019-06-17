package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testprep.R
import com.testprep.activity.PackageDetailActivity
import com.testprep.models.PackageData
import com.testprep.utils.Utils

class TestPackagesAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
    RecyclerView.Adapter<TestPackagesAdapter.viewholder>() {

    var row_index = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
//            LayoutInflater.from(context).inflate(R.layout.list_item_test_package, p0, false)
            LayoutInflater.from(context).inflate(R.layout.new_prefrence_item_layout, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        p0.std.text = dataList[p1].TestPackageName
////        p0.sub.text = "Mathematics"
////        p0.price.text = "₹" + dataList[p1].TestPackageSalePrice

        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].TestPackageName.substring(0, 1)))

        p0.title.text = dataList[p1].TestPackageName
//        p0.stitle.text = dataList[p1].TestPackageName

        p0.image.setOnClickListener {

            p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))

            Utils.setStringValue(context, "standard_name", dataList[p1].TestPackageName)

            row_index = p1
            notifyDataSetChanged()

            val intent = Intent(context, PackageDetailActivity::class.java)
            intent.putExtra("pname", p0.title.text!!)
            intent.putExtra("sprice", dataList[p1].TestPackageSalePrice)
            intent.putExtra("lprice", dataList[p1].TestPackageListPrice)
            intent.putExtra("desc", dataList[p1].TestPackageDescription)
            intent.putExtra("test_type_list", dataList[p1].TestType)
            intent.putExtra("position", dataList[p1].TestPackageName.substring(0, 1).single())
            context.startActivity(intent)

        }

        if (row_index == p1) {
            p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))
            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
            p0.p_select.visibility = View.VISIBLE
        } else {
            p0.title.setTextColor(context.resources.getColor(R.color.black))
            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
            p0.p_select.visibility = View.GONE
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.package_image)
        var title: TextView = itemView.findViewById(R.id.package_name)
        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
        var p_select: ImageView = itemView.findViewById(R.id.package_select)

//        var std: TextView = itemView.findViewById(R.id.singleTest_item_tvStd)
//        var sub: TextView = itemView.findViewById(R.id.singleTest_item_tvSub)
//        var price: TextView = itemView.findViewById(R.id.singleTest_item_tvPrice)
//        var addcart: TextView = itemView.findViewById(R.id.singleTest_item_btnAddCart)
    }
}

