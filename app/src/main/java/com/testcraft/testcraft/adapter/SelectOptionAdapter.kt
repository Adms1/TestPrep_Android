package com.testcraft.testcraft.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.QuestionResponse

class SelectOptionAdapter(
    val context: Context,
    val dataList: ArrayList<QuestionResponse.QuestionDataList>
) :
    RecyclerView.Adapter<SelectOptionAdapter.viewholder>() {

    private var lastCheckedRadioGroup: RadioGroup? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.select_option_list_view,
                p0,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//        p0.opone1.loadData(
//            dataList[p1].title + "<div></div>" + dataList[p1].titlehtml,
//            "text/html",
//            "UTF-8"
//        )

        var id = (p1 + 1) * 100
//        for (i in 0..4) {
        val rb = RadioButton(this@SelectOptionAdapter.context)

        rb.id = id++

//            when (p1) {
//                0 -> {
//                    rb.text = "A"
//                }
//                1 -> {
//                    rb.text = "B"
//                }
//                2 -> {
//                    rb.text = "C"
//                }
//                3 -> {
//                    rb.text = "D"
//                }
//            }


        p0.opone.addView(rb)
//        }


        p0.opone.setOnCheckedChangeListener { group, checkedId ->

            //since only one package is allowed to be selected
            //this logic clears previous selection
            //it checks state of last radiogroup and
            // clears it if it meets conditions
            if (lastCheckedRadioGroup != null
                && lastCheckedRadioGroup!!.checkedRadioButtonId
                != p0.opone.checkedRadioButtonId
                && lastCheckedRadioGroup!!.checkedRadioButtonId != -1
            ) {
                lastCheckedRadioGroup!!.clearCheck()

//                    Toast.makeText(this@SelectOptionAdapter.context,
//                        "Radio button clicked " + p0.opone.checkedRadioButtonId,
//                        Toast.LENGTH_SHORT).show()

            }
            lastCheckedRadioGroup = p0.opone
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var opone1: WebView = itemView.findViewById(R.id.option_one1)
        var opone: RadioGroup = itemView.findViewById(R.id.option_one)

    }
}
