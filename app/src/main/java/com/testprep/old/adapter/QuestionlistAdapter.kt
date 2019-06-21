package com.testprep.old.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.testprep.R.id
import com.testprep.R.layout
import com.testprep.old.models.QuestionResponse

class QuestionlistAdapter(val context: Context, val dataList: ArrayList<QuestionResponse.QuestionList>) : RecyclerView.Adapter<QuestionlistAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                layout.question_list_item,
                p0,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

//            p0.quesimg.loadData(
//                "<b>Q." + (p1 + 1) + "</b> " + dataList[p1].title + dataList[p1].titlehtml,
//                "text/html",
//                "UTF-8"
//            )
//
//            p0.optwo1.loadData(
//                dataList[p1].mcq[0].title + "<div></div>" + dataList[p1].mcq[0].titlehtml,
//                "text/html",
//                "UTF-8"
//            )
//            p0.opone1.loadData(
//                dataList[p1].mcq[1].title + "<div></div>" + dataList[p1].mcq[1].titlehtml,
//                "text/html",
//                "UTF-8"
//            )
//            p0.opthree1.loadData(
//                dataList[p1].mcq[2].title + "<div></div>" + dataList[p1].mcq[2].titlehtml,
//                "text/html",
//                "UTF-8"
//            )
//            p0.opfour1.loadData(
//                dataList[p1].mcq[3].title + "<div></div>" + dataList[p1].mcq[3].titlehtml,
//                "text/html",
//                "UTF-8"
//            )

            p0.opone.setOnCheckedChangeListener { group, checkedId ->

                if (checkedId) {
                    p0.opone.isChecked = true
                    p0.optwo.isChecked = false
                    p0.opthree.isChecked = false
                    p0.opfour.isChecked = false
                }
            }

            p0.optwo.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId) {
                    p0.opone.isChecked = false
                    p0.optwo.isChecked = true
                    p0.opthree.isChecked = false
                    p0.opfour.isChecked = false
                }
            }

            p0.opthree.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId) {
                    p0.opone.isChecked = false
                    p0.optwo.isChecked = false
                    p0.opthree.isChecked = true
                    p0.opfour.isChecked = false
                }
            }

            p0.opfour.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId) {
                    p0.opone.isChecked = false
                    p0.optwo.isChecked = false
                    p0.opthree.isChecked = false
                    p0.opfour.isChecked = true
                }
            }
    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var quesimg: WebView = itemView.findViewById(id.que_img)
            var opone1: WebView = itemView.findViewById(id.option_one1)
            var optwo1: WebView = itemView.findViewById(id.option_two1)
            var opthree1: WebView = itemView.findViewById(id.option_three1)
            var opfour1: WebView = itemView.findViewById(id.option_four1)

            var que_grp: RadioGroup = itemView.findViewById(id.que_grp)
            var opone: RadioButton = itemView.findViewById(id.option_one)
            var optwo: RadioButton = itemView.findViewById(id.option_two)
            var opthree: RadioButton = itemView.findViewById(id.option_three)
            var opfour: RadioButton = itemView.findViewById(id.option_four)
//        var gr = GRadioGroup(opone, optwo, opthree, opfour)

    }
}
