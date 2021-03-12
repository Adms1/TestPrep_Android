package com.testcraft.testcraft.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.activity.DashboardActivity.Companion.setFragments
import com.testcraft.testcraft.fragments.MyPackagesFragment.Companion.callInsertSubscriptionConfirm
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.*

@SuppressLint("SetTextI18n")
class MyPackageAdapter(
    val context: Context,
    val dataList: ArrayList<PackageData.PackageDataList>, val come_from: String, val isExpired: String
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
//        p0.tutor.text = "By " + dataList[p1].TutorName

        if (p1 == dataList.size - 1) {
            p0.ivLine.visibility = View.GONE
        } else {
            p0.ivLine.visibility = View.VISIBLE
        }

        if (dataList[p1].NumberOfComletedTest == dataList[p1].NumberOfTest) {
            p0.ivComplete.visibility = View.VISIBLE
        } else {
            p0.ivComplete.visibility = View.GONE
        }

        Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon).placeholder(R.mipmap.progressicn).into(p0.image)

        if (come_from == "market_place") {

            p0.price.text = dataList[p1].TestPackageSalePrice

            p0.subject.text = dataList[p1].SubjectName + " | " + dataList[p1].TutorName

            p0.tutor.visibility = View.INVISIBLE
            p0.test.visibility = View.GONE
            p0.ivArrow.visibility = View.GONE

            if (dataList[p1].TestPackageSalePrice.equals("free", true)) {

                p0.btnStart.visibility = View.VISIBLE
                p0.price.visibility = View.GONE

            } else {

                p0.price.visibility = View.VISIBLE
                p0.btnStart.visibility = View.GONE
            }


        } else if (come_from == "my_pkgs") {

            p0.price.visibility = View.GONE
            p0.btnStart.visibility = View.GONE

            p0.test.visibility = View.VISIBLE
            p0.ivArrow.visibility = View.VISIBLE
            p0.tutor.visibility = View.GONE

            p0.subject.text = dataList[p1].TutorName

            if (dataList[p1].NumberOfTest == "1") {

                p0.test.text =
                    dataList[p1].NumberOfComletedTest + "/" + dataList[p1].NumberOfTest + " Test"

            } else {

                p0.test.text =
                    dataList[p1].NumberOfComletedTest + "/" + dataList[p1].NumberOfTest + " Tests"

            }

        }

//        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].TestPackageName.substring(0, 1)))

        p0.btnStart.setOnClickListener {

            CommonWebCalls.callToken(context, "1", "", ActionIdData.C1101, ActionIdData.T1101)

//            if (Utils.getStringValue(context, AppConstants.IS_LOGIN, "") == "true") {
            DialogUtils.createConfirmDialog(
                context,
                "",
                "Are you sure you want to buy this package?",
                "Yes",
                "No",
                DialogInterface.OnClickListener { dialog, which ->

                    if (DialogUtils.isNetworkConnected(context)) {

                        PackagePurchase.callAddToCart("freetest", dataList[p1].TestPackageID, context, "")

                    } else {
                        Utils.ping(context, AppConstants.NETWORK_MSG)
                    }

                },
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()

                }).show()
//            } else {
//                val intent = Intent(context, IntroActivity::class.java)
//                (context as DashboardActivity).startActivity(intent)
//            }
        }

        p0.mainll.setOnClickListener {

            if (come_from == "market_place") {

                CommonWebCalls.callToken(context, "1", "", ActionIdData.C808, ActionIdData.T808)

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

                if (isExpired != "1") {
                    CommonWebCalls.callToken(context, "1", "", ActionIdData.C1702, ActionIdData.T1702)

                    AppConstants.PKG_ID = dataList[p1].StudentTestPackageID.toString()
                    AppConstants.PKG_NAME = dataList[p1].TestPackageName
                    AppConstants.IS_SELF_TEST = "false"

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
                } else {
                    DialogUtils.createConfirmDialog(context, "Alert",
                        context.getString(R.string.subscription_expire_popup_msg),
                        "Pay Later", "Pay Now",

                        DialogInterface.OnClickListener { dialog, which ->

                            dialog.dismiss()
                        },
                        DialogInterface.OnClickListener { dialog, which ->

                            callInsertSubscriptionConfirm(context)

                        }).show()
                }
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

        var subject: TextView = itemView.findViewById(R.id.item_my_package_subject)
        var tutor: TextView = itemView.findViewById(R.id.item_my_package_tutor)
        var ivComplete: ImageView = itemView.findViewById(R.id.main_package_select)
        var name: TextView = itemView.findViewById(R.id.item_my_package_name)

        //        var short_name: TextView = itemView.findViewById(R.id.item_my_package_name_short)
        var mainll: ConstraintLayout = itemView.findViewById(R.id.item_my_package_main)
        var price: TextView = itemView.findViewById(R.id.item_my_package_price)
        var btnStart: Button = itemView.findViewById(R.id.item_my_package_btnAddTocart)
        var test: TextView = itemView.findViewById(R.id.item_my_package_test)
        var image: ImageView = itemView.findViewById(R.id.item_my_package_image)
        var ivArrow: ImageView = itemView.findViewById(R.id.item_my_package_btnNext)
        var ivLine: View = itemView.findViewById(R.id.item_my_package_line)

//        var std: TextView = itemView.findViewById(R.id.package_item_tvStd)
    }

}
