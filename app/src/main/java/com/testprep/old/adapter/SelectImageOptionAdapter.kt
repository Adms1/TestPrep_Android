package com.testprep.old.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.activity.TabwiseQuestionActivity
import com.testprep.old.models.QuestionResponse

class SelectImageOptionAdapter(
    val context: Context,
    val dataList: ArrayList<QuestionResponse.QuestionDataList>,
    var qsize: Int,
    var qtype: Int,
    var qid: String
) : RecyclerView.Adapter<SelectImageOptionAdapter.viewholder>() {

    private var lastCheckedRadioGroup: RadioGroup? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.select_image_option_list_view,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        if ("http://content.testcraft.co.in/question/" + dataList[p1].AnswerImage != "") {

            Log.d("qsize", "" + qsize)

            Picasso.get().load("http://content.testcraft.co.in/question/" + dataList[p1].AnswerImage)
                .resize(qsize, p0.opone1.height)
//                .fit()
//                .centerInside()
                .into(p0.opone1)
        }

        var id = (p1 + 1) * 100

        when (qtype) {
            1 -> {

                p0.opone.visibility = View.VISIBLE
                p0.opone1.visibility = View.VISIBLE
                p0.llCheckBox.visibility = View.GONE

                val rb = RadioButton(this@SelectImageOptionAdapter.context)

                rb.layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )

                rb.id = id++

                p0.opone.addView(rb)

            }
            2 -> {

                p0.opone.visibility = View.GONE
                p0.opone1.visibility = View.VISIBLE
                p0.llCheckBox.visibility = View.VISIBLE

                val cb = CheckBox(this@SelectImageOptionAdapter.context)

                p0.llCheckBox.addView(cb)

            }

        }

        p0.opone.setOnCheckedChangeListener { group, checkedId ->

            TabwiseQuestionActivity.setButton(dataList[p1].MultipleChoiceQuestionAnswerID, qid, true)

            if (lastCheckedRadioGroup != null
                && lastCheckedRadioGroup!!.checkedRadioButtonId
                != p0.opone.checkedRadioButtonId
                && lastCheckedRadioGroup!!.checkedRadioButtonId != -1

            ) {
                lastCheckedRadioGroup!!.clearCheck()

            } else if (lastCheckedRadioGroup == null) {

            } else {

            }
            lastCheckedRadioGroup = p0.opone
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var opone1: ImageView = itemView.findViewById(R.id.option_one1)
        var opone: RadioGroup = itemView.findViewById(R.id.option_one)
        var llmain: RelativeLayout = itemView.findViewById(R.id.option_lll)
        var llCheckBox: LinearLayout = itemView.findViewById(R.id.option_llCheckbox)
    }

}
