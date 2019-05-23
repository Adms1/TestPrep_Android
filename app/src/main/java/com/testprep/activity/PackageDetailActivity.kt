package com.testprep.activity

import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.testprep.R
import com.testprep.utils.Utils
import kotlinx.android.synthetic.main.activity_package_detail.*

class PackageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_detail)

        package_detail_tvPname.text = intent.getStringExtra("pname")
        package_detail_tvsprice.text = "Sell Price : " + intent.getStringExtra("sprice")
        package_detail_tvlprice.text = "List Price : " + intent.getStringExtra("lprice")
        package_detail_tvDesc.text = intent.getStringExtra("desc")

        var pos = intent.getIntExtra("position", 0)

        package_detail_image1.setImageDrawable(Utils.createDrawable(pos))
        package_detail_tvlprice.paintFlags = package_detail_tvlprice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        package_detail_ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}
