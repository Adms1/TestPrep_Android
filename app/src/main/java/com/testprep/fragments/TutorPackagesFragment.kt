package com.testprep.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.testprep.R
import com.testprep.activity.CartActivity
import com.testprep.adapter.PopularPackagesAdapter
import com.testprep.adapter.TestPackagesAdapter
import com.testprep.models.PackageData
import kotlinx.android.synthetic.main.fragment_tutor_packages.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TutorPackagesFragment : AppCompatActivity() {

    var dataList: ArrayList<PackageData.PackageDataList> = ArrayList()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.fragment_tutor_packages)

        tutor_packages_rvPopularPkg.isNestedScrollingEnabled = false
        tutor_packages_rvPopularTest.isNestedScrollingEnabled = false

        tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(this@TutorPackagesFragment, 2)
        tutor_packages_rvPopularTest.layoutManager =
            LinearLayoutManager(this@TutorPackagesFragment, LinearLayoutManager.VERTICAL, false)

        tutor_packages_rvPopularPkg.adapter = PopularPackagesAdapter(this@TutorPackagesFragment)
        tutor_packages_rvPopularTest.adapter = TestPackagesAdapter(this@TutorPackagesFragment, dataList)

        tutor_packages_ivCart.setOnClickListener {
            val intent = Intent(this@TutorPackagesFragment, CartActivity::class.java)
            startActivity(intent)
        }

        tutor_packages_ivBack.setOnClickListener { onBackPressed() }

        tutor_packages_tvViewProfile.setOnClickListener {

            val intent = Intent(this@TutorPackagesFragment, TutorProfileFragment::class.java)
            startActivity(intent)

        }

    }

}
