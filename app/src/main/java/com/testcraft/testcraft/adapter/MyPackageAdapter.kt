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
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.AppConstants

class MyPackageAdapter(
    val context: Context,
    val dataList: ArrayList<PackageData.PackageDataList>, val come_from: String
) : RecyclerView.Adapter<MyPackageAdapter.viewholder>() {

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

        p0.name.text = dataList[p1].TestPackageName

        if (dataList[p1].NumberOfComletedTest == dataList[p1].NumberOfTest) {
            p0.ivComplete.visibility = View.VISIBLE
        } else {
            p0.ivComplete.visibility = View.GONE
        }

        if (dataList[p1].NumberOfTest == "1") {

            p0.test.text =
                dataList[p1].NumberOfComletedTest + "/" + dataList[p1].NumberOfTest + " Test"

        } else {

            p0.test.text =
                dataList[p1].NumberOfComletedTest + "/" + dataList[p1].NumberOfTest + " Tests"

        }

        Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon).into(p0.image)

        if (come_from == "market_place") {

            p0.price.text = dataList[p1].TestPackageSalePrice

            p0.sdate.text = dataList[p1].SubjectName
            p0.tutor.text = "By " + dataList[p1].TutorName
            p0.test.visibility = View.GONE
            p0.tutor.visibility = View.VISIBLE

        } else if (come_from == "my_pkgs") {

            p0.price.text = ""
            p0.tutor.visibility = View.GONE
            p0.sdate.text = dataList[p1].TutorName
            p0.test.visibility = View.VISIBLE
        }

//        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].TestPackageName.substring(0, 1)))

        p0.mainll.setOnClickListener {

            if (come_from == "market_place") {

                AppConstants.isFirst = 14
                val bundle = Bundle()
                bundle.putString("pkgid", dataList[p1].TestPackageID)
                bundle.putString("come_from", "selectpackage")
                setFragments(bundle)


//                val intent = Intent(context, PackageDetailActivity::class.java)
//                intent.putExtra("pkgid", dataList[p1].TestPackageID)
//                intent.putExtra("tutor_id", dataList[p1].TutorID)
//                intent.putExtra("come_from", "selectpackage")
//                context.startActivity(intent)

            } else if (come_from == "my_pkgs") {

                AppConstants.PKG_ID = dataList[p1].StudentTestPackageID.toString()
                AppConstants.PKG_NAME = dataList[p1].TestPackageName

                AppConstants.isFirst = 12
//                val bundle = Bundle()
//                bundle.putString("pkgid", dataList[p1].StudentTestPackageID.toString())
//                bundle.putString("pname", dataList[p1].TestPackageName)
                setFragments(null)

//                val intent1 = Intent(context, DashboardActivity::class.java)
//                intent1.putExtra("pkgid", dataList[p1].StudentTestPackageID.toString())
//                intent1.putExtra("pname", dataList[p1].TestPackageName)
//                intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                context.startActivity(intent1)
//                (context as DashboardActivity).finish()


//                val intent = Intent(context, TestListActivity::class.java)
//                intent.putExtra("pkgid", dataList[p1].StudentTestPackageID.toString())
//                intent.putExtra("pname", dataList[p1].TestPackageName)
//                context.startActivity(intent)
            }

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
        var tutor: TextView = itemView.findViewById(R.id.item_my_package_tutor)
        var ivComplete: ImageView = itemView.findViewById(R.id.main_package_select)
        var name: TextView = itemView.findViewById(R.id.item_my_package_name)
        //        var short_name: TextView = itemView.findViewById(R.id.item_my_package_name_short)
        var mainll: ConstraintLayout = itemView.findViewById(R.id.item_my_package_main)
        var price: TextView = itemView.findViewById(R.id.item_my_package_price)
        //        var view: Button = itemView.findViewById(R.id.item_my_package_view)
        var test: TextView = itemView.findViewById(R.id.item_my_package_test)
        var image: ImageView = itemView.findViewById(R.id.item_my_package_image)

//        var std: TextView = itemView.findViewById(R.id.package_item_tvStd)
    }

}