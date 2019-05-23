package com.testprep.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.testprep.activity.PackageDetailActivity
import com.testprep.models.PackageData
import com.testprep.utils.Utils


class SelectPackageAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
    RecyclerView.Adapter<SelectPackageAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(com.testprep.R.layout.list_item_select_package, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        if (dataList[p1].image != 0) {
//
//            p0.image.visibility = GONE
//            p0.image1.visibility = VISIBLE
//
//            p0.image1.setImageResource(dataList[p1].imagee)
//
//        } else {
//
//            p0.image1.visibility = GONE
//            p0.image.visibility = VISIBLE
//            p0.pname.visibility = VISIBLE
////            createDrawable
//
//            p0.pname.text = dataList[p1].namee.substring(0, 1)
//
//            val drawable = mDrawableBuilder.build(dataList[p1].TestPackageName.substring(0, 1), mColorGenerator.getColor(dataList[p1]))
        p0.image.setImageDrawable(Utils.createDrawable(p1))
//        }
//
//        p0.itemView.setBackgroundColor(Color.TRANSPARENT)
//
        p0.name.text = dataList[p1].TestPackageName
        p0.sprice.text = "₹" + dataList[p1].TestPackageSalePrice.replaceAfter(".", "").replace(".", "")
        p0.lprice.text = " ₹" + dataList[p1].TestPackageListPrice.replaceAfter(".", "").replace(".", "")

//        val mBSpannableString = SpannableString( p0.lprice.text)
//        val mStrikeThrough = StrikethroughSpan()
//        mBSpannableString.setSpan(mStrikeThrough, 0, p0.lprice.text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        p0.lprice.paintFlags = p0.lprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//        p0.lprice.paintFlags = p0.lprice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG

        p0.llmain.setOnClickListener {

            val intent = Intent(context, PackageDetailActivity::class.java)
            intent.putExtra("pname", p0.name.text!!)
            intent.putExtra("sprice", p0.sprice.text!!)
            intent.putExtra("lprice", p0.lprice.text!!)
            intent.putExtra("desc", dataList[p1].TestPackageDescription)
            intent.putExtra("position", p1)
            context.startActivity(intent)

        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(com.testprep.R.id.package_image)
        //        var image1: CircleImageView = itemView.findViewById(R.id.package_image1)
        var name: TextView = itemView.findViewById(com.testprep.R.id.package_name)
        var sprice: TextView = itemView.findViewById(com.testprep.R.id.package_sprice)
        var lprice: TextView = itemView.findViewById(com.testprep.R.id.package_lprice)
        //        var pname: TextView = itemView.findViewById(R.id.package_name_short)
        var llmain: CardView = itemView.findViewById(com.testprep.R.id.package_llmain)
    }

}
