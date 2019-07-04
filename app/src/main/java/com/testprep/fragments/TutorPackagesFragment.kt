package com.testprep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.testprep.R
import com.testprep.adapter.PopularPackagesAdapter
import com.testprep.adapter.TestPackagesAdapter
import com.testprep.models.PackageData
import kotlinx.android.synthetic.main.fragment_tutor_packages.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TutorPackagesFragment : Fragment() {

    var dataList: ArrayList<PackageData.PackageDataList> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutor_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutor_packages_rvPopularPkg.isNestedScrollingEnabled = false
        tutor_packages_rvPopularTest.isNestedScrollingEnabled = false

        tutor_packages_rvPopularPkg.layoutManager = GridLayoutManager(activity, 2)
        tutor_packages_rvPopularTest.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        tutor_packages_rvPopularPkg.adapter = PopularPackagesAdapter(activity!!)
        tutor_packages_rvPopularTest.adapter = TestPackagesAdapter(activity!!, dataList)

    }

}
