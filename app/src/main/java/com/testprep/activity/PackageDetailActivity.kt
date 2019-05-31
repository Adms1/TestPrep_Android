package com.testprep.activity

import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.testprep.R
import com.testprep.adapter.TestTypeAdapter
import com.testprep.models.PackageData
import kotlinx.android.synthetic.main.activity_package_detail.*

class PackageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_detail)

        package_detail_tvPname.text = intent.getStringExtra("pname")
        package_detail_tvsprice.text = "Sell Price : " + intent.getStringExtra("sprice")
        package_detail_tvlprice.text = "List Price : " + intent.getStringExtra("lprice").trim()
        package_detail_tvDesc.text = intent.getStringExtra("desc")
        package_detail_name_short.text = intent.getStringExtra("pname").substring(0, 1)

        var pos = intent.getCharExtra("position", 'a')

        Log.d("colr1", " " + pos + " " + package_detail_tvPname.text.length)

//        package_detail_image1.setImageDrawable(Utils.createDrawable(pos, package_detail_tvPname.text.length))
        package_detail_tvlprice.paintFlags = package_detail_tvlprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        package_detail_rvList.isNestedScrollingEnabled = false
        package_detail_rvList.layoutManager =
            LinearLayoutManager(this@PackageDetailActivity, LinearLayoutManager.VERTICAL, false)

        package_detail_rvList.adapter = TestTypeAdapter(
            this@PackageDetailActivity,
            intent.getSerializableExtra("test_type_list") as ArrayList<PackageData.PackageTestType>
        )

        package_detail_ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}
