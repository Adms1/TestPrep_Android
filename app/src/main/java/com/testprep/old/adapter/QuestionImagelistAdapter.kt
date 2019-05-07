package com.testprep.old.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.old.models.QuestionResponse

class QuestionImagelistAdapter(val context: Context, val dataList: ArrayList<QuestionResponse.QuestionList>) : RecyclerView.Adapter<QuestionImagelistAdapter.viewholder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.question_list_item1,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        if("http://content.testcraft.co.in/question/" + dataList[p1].titleimg != "") {
            Picasso.get().load("http://content.testcraft.co.in/question/" + dataList[p1].titleimg).into(p0.quesimg)
        }
        if("http://content.testcraft.co.in/question/" + dataList[p1].mcq[0].titleimg != "") {
            Picasso.get().load("http://content.testcraft.co.in/question/" + dataList[p1].mcq[0].titleimg).into(p0.opone1)
        }
        if("http://content.testcraft.co.in/question/" + dataList[p1].mcq[1].titleimg != "") {
            Picasso.get().load("http://content.testcraft.co.in/question/" + dataList[p1].mcq[1].titleimg).into(p0.optwo1)
        }
        if("http://content.testcraft.co.in/question/" + dataList[p1].mcq[2].titleimg != "") {
            Picasso.get().load("http://content.testcraft.co.in/question/" + dataList[p1].mcq[2].titleimg).into(p0.opthree1)
        }
        if("http://content.testcraft.co.in/question/" + dataList[p1].mcq[3].titleimg != "") {
            Picasso.get().load("http://content.testcraft.co.in/question/" + dataList[p1].mcq[3].titleimg).into(p0.opfour1)
        }

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

            var quesimg: ImageView = itemView.findViewById(R.id.img_que_img)
            var opone1: ImageView = itemView.findViewById(R.id.option_one1)
            var optwo1: ImageView = itemView.findViewById(R.id.option_two1)
            var opthree1: ImageView = itemView.findViewById(R.id.option_three1)
            var opfour1: ImageView = itemView.findViewById(R.id.option_four1)

            var que_grp: RadioGroup = itemView.findViewById(R.id.que_grp)
            var opone: RadioButton = itemView.findViewById(R.id.option_one)
            var optwo: RadioButton = itemView.findViewById(R.id.option_two)
            var opthree: RadioButton = itemView.findViewById(R.id.option_three)
            var opfour: RadioButton = itemView.findViewById(R.id.option_four)
//        var gr = GRadioGroup(opone, optwo, opthree, opfour)

    }
}
