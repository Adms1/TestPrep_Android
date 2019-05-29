package com.testprep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testprep.R
import com.testprep.adapter.MyPackageAdapter
import kotlinx.android.synthetic.main.fragment_my_packages.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyPackagesFragment : Fragment() {

    private var packageSize: ArrayList<Int> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val heading = activity!!.findViewById(R.id.dashboard_tvTitle) as TextView
        heading.text = "My Packages"

        packageSize.add(23)
        packageSize.add(25)
        packageSize.add(27)
        packageSize.add(29)
        packageSize.add(22)

        my_packages_rvList.layoutManager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)

//        val dividerItemDecoration = DividerItemDecoration(
//            my_packages_rvList.context,
//            LinearLayoutManager.VERTICAL
//        )
//        my_packages_rvList.addItemDecoration(dividerItemDecoration)
        my_packages_rvList.adapter = MyPackageAdapter(activity!!, packageSize)

    }

}
