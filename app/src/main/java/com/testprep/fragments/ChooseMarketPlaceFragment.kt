package com.testprep.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R
import com.testprep.adapter.MainPackageAdapter
import kotlinx.android.synthetic.main.fragment_choose_market_place.*

class ChooseMarketPlaceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_market_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        choosemp_rvList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        choosemp_rvList.adapter = MainPackageAdapter(activity!!)

    }
}
