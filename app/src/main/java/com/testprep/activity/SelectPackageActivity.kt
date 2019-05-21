package com.testprep.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.amulyakhare.textdrawable.TextDrawable
import com.testprep.R
import com.testprep.adapter.SelectPackageAdapter
import com.testprep.models.PackageData
import kotlinx.android.synthetic.main.activity_select_package.*


class SelectPackageActivity : AppCompatActivity() {

    var selectPackageAdapter: SelectPackageAdapter? = null

    private var mDrawableBuilder: TextDrawable.IBuilder? = null

    // list of data items
    private val mDataList: ArrayList<PackageData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_select_package)

        mDataList.add(PackageData("Iron Man", R.drawable.ironman, "$ 100"))
        mDataList.add(PackageData("Captain America", 0, "$ 200"))
        mDataList.add(PackageData("James Bond", 0, "$ 500"))
        mDataList.add(PackageData("Harry Potter", R.drawable.harrypotter, "$ 300"))
        mDataList.add(PackageData("Sherlock Holmes", 0, "$ 700"))
        mDataList.add(PackageData("Black Widow", R.drawable.blackwidow, "$ 400"))
        mDataList.add(PackageData("Hawk Eye", 0, "$ 5100"))
        mDataList.add(PackageData("Iron Man", 0, "$ 200"))
        mDataList.add(PackageData("Guava", 0, "$ 600"))
        mDataList.add(PackageData("Tomato", 0, "$ 400"))
        mDataList.add(PackageData("Sherlock Holmes", 0, "$ 100"))
        mDataList.add(PackageData("Strawberry", 0, "$ 6700"))
        mDataList.add(PackageData("Watermelon", 0, "$ 200"))
        mDataList.add(PackageData("Pears", 0, "$ 500"))
        mDataList.add(PackageData("Kiwi", R.drawable.kiwi, "$ 800"))

        package_rvList.layoutManager = GridLayoutManager(this@SelectPackageActivity, 2)

        mDrawableBuilder = TextDrawable.builder().round()

        selectPackageAdapter = SelectPackageAdapter(this@SelectPackageActivity, mDataList)
        package_rvList.adapter = selectPackageAdapter

        package_ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}
