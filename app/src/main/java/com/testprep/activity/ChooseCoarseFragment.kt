package com.testprep.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.testprep.R
import com.testprep.adapter.ChooseCoarseAdapter
import kotlinx.android.synthetic.main.fragment_choose_coarse.*

/**
 * A simple [Fragment] subclass.
 *
 */

class ChooseCoarseFragment : Fragment() {

    private var chooseCoarseAdapter: ChooseCoarseAdapter? = null
    private var coarseList = arrayOf("Competitive", "Academic")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_coarse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coarse_rvCoarseList.layoutManager = GridLayoutManager(activity!!, 2)
        chooseCoarseAdapter = ChooseCoarseAdapter(activity!!, coarseList)
        coarse_rvCoarseList.adapter = chooseCoarseAdapter

    }

}
