package com.testprep.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testprep.R
import io.apptik.widget.MultiSlider
import kotlinx.android.synthetic.main.fragment_price_filter.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PriceFilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_price_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        price_filter_etMin.setText("₹ " + price_filter_range_slider.getThumb(0).value.toString())
        price_filter_etMax.setText("₹ " + price_filter_range_slider.getThumb(1).value.toString())

        price_filter_range_slider.setOnThumbValueChangeListener(object : MultiSlider.SimpleChangeListener() {
            override fun onValueChanged(
                multiSlider: MultiSlider?,
                thumb: MultiSlider.Thumb?,
                thumbIndex: Int,
                value: Int
            ) {
                if (thumbIndex == 0) {
                    price_filter_etMin.setText("₹ " + value.toString())
                } else {
                    price_filter_etMax.setText("₹ " + value.toString())
                }
            }
        })

    }
}
