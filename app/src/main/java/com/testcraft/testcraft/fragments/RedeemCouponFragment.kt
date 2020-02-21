package com.testcraft.testcraft.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testcraft.testcraft.R
import com.testcraft.testcraft.adapter.UsedCouponAdapter
import com.testcraft.testcraft.models.CouponModel
import kotlinx.android.synthetic.main.fragment_redeem_coupon.*

/**
 * A simple [Fragment] subclass.
 */
class RedeemCouponFragment : Fragment() {

    var list: ArrayList<CouponModel.dataa> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_redeem_coupon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ob1 = CouponModel.dataa()
        ob1.CouponCode = "FIRST FREE"
        ob1.CouponDiscription = "To get first free test. use only once."
        ob1.ExpireDate = "Appplied date :  04 Jan 2020"
        list.add(ob1)

        val ob2 = CouponModel.dataa()
        ob2.CouponCode = "WINTER"
        ob2.CouponDiscription = "share with friend and get free test. use only once.."
        ob2.ExpireDate = "Appplied date :  15 Feb 2020"
        list.add(ob2)

        redeem_rvCoupon.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        redeem_rvCoupon.adapter = UsedCouponAdapter(activity!!, list)

    }

}
