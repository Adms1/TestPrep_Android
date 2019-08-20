package com.testprep.old.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.RelativeLayout
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.old.models.QuestionResponse

class SolutionAdapter(
    val context: Context,
    val dataList: ArrayList<QuestionResponse.QuestionDataList>,
    var qsize: Int,
    var qType: Int
) :
    RecyclerView.Adapter<SolutionAdapter.viewholder>() {

    private var lastCheckedRadioGroup: RadioGroup? = null
    private val lastCheckedPosition = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.solution_list_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        if (qType == 1) {
            if ("http://content.testcraft.co.in/question/" + dataList[p1].AnswerImage != "") {

                Log.d("qsize", "" + qsize)

                var imgwidth: Int = 100

                Picasso.get().load("http://content.testcraft.co.in/question/" + dataList[p1].AnswerImage)
                    .resize(qsize, p0.opone1.height)
//                .fit()
//                .centerInside()
                    .into(p0.opone1)
            }

//        if(dataList[p1].IsCorrectAnswer){
//
//                p0.opone.setImageResource(R.drawable.green_round)
//
//
//
//        }else{
//
//        }

            when {
                dataList[p1].IsCorrectAnswer -> p0.opone.setImageResource(R.drawable.wrong)
                dataList[p1].IsUserSelected -> p0.opone.setImageResource(R.drawable.correct)
                else -> p0.opone.setImageResource(R.drawable.grey_round)
            }
        } else {

        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var opone1: ImageView = itemView.findViewById(R.id.option_one1)
        var opone: ImageView = itemView.findViewById(R.id.option_one)
        var llmain: RelativeLayout = itemView.findViewById(R.id.option_lll)
    }

}
