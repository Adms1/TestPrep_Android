package com.testprep.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R
import com.testprep.utils.AppConstants
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

        AppConstants.ON_BACK = 1

        choosemp_tvSingleTest.setOnClickListener {}

        choosemp_tvTestPackages.setOnClickListener {

            fragmentManager!!.beginTransaction().replace(R.id.container, MarketPlaceFragment()).addToBackStack(null)
                .commit()

//            val intent = Intent(activity, SelectPackageActivity::class.java)
//            startActivity(intent)
        }

//        choosemp_ivFilter.setOnClickListener {
//            val intent = Intent(activity, FilterActivity::class.java)
//            startActivity(intent)
//        }

        choosemp_tvTutors.setOnClickListener {}
    }
}
