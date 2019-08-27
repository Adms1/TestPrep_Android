package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.activity.TestListActivity
import com.testprep.models.PackageData

class MyPackageAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
    RecyclerView.Adapter<MyPackageAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.list_item_my_package, p0, false)
//            LayoutInflater.from(context).inflate(R.layout.popular_pkg_list_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.sdate.text = dataList[p1].PurchaseDate
//        p0.edate.text = dataList[p1].ExpirationDate
        p0.name.text = dataList[p1].TestPackageName
        p0.short_name.text = p0.name.text.substring(0, 1)
        p0.price.text = "Price: ₹" + dataList[p1].TestPackageSalePrice
        p0.test.text = dataList[p1].NumberOfTest + " Tests"
//
////        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].TestPackageName.substring(0, 1)))
//
        p0.mainll.setOnClickListener {

            val intent = Intent(context, TestListActivity::class.java)
            intent.putExtra("pkgid", dataList[p1].StudentTestPackageID.toString())
            intent.putExtra("pname", dataList[p1].TestPackageName)
            context.startActivity(intent)

        }
//
//        p0.view.setOnClickListener {
//
//            Log.d("pkgid", dataList[p1].StudentTestPackageID.toString())
//
//            val intent = Intent(context, PackageDetailActivity::class.java)
//            intent.putExtra("pkgid", dataList[p1].StudentTestPackageID.toString())
//            intent.putExtra("pname", dataList[p1].TestPackageName)
//            intent.putExtra("sprice", dataList[p1].TestPackageSalePrice)
//            intent.putExtra("lprice", "")
//            intent.putExtra("desc", "")
//            intent.putExtra("created_by", "")
//            intent.putExtra("test_type_list", dataList[p1].TestType)
//            intent.putExtra("come_from", "mypackage")
//            intent.putExtra("position", dataList[p1].TestPackageName.substring(0, 1).single())
//            context.startActivity(intent)
//        }
//
//        p0.test.setOnClickListener {
//
//        }

//        p0.std.setTextColor(context.resources.getColor(R.color.white))
//        p0.std.text = dataList[p1].TestPackageName

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var sdate: TextView = itemView.findViewById(R.id.item_my_package_sdate)
//        var edate: TextView = itemView.findViewById(R.id.item_my_package_edate)
//        var image: ImageView = itemView.findViewById(R.id.item_my_package_image)
var name: TextView = itemView.findViewById(R.id.item_my_package_name)
        var short_name: TextView = itemView.findViewById(R.id.item_my_package_name_short)
        var mainll: ConstraintLayout = itemView.findViewById(R.id.item_my_package_main)
        var price: TextView = itemView.findViewById(R.id.item_my_package_price)
//        var view: Button = itemView.findViewById(R.id.item_my_package_view)
var test: TextView = itemView.findViewById(R.id.item_my_package_test)

//        var std: TextView = itemView.findViewById(R.id.package_item_tvStd)
    }

}
