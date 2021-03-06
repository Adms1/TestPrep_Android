package com.testcraft.testcraft.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.sectionmodule.NewQuestionResponse
import com.testcraft.testcraft.utils.transform

class SolutionAdapter(
    val context: Context,
    val dataList: ArrayList<NewQuestionResponse.QuestionDataList>,
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

        p0.abcd.text = dataList[p1].optiontext

        if (qType == 1 || qType == 7) {

            if (dataList[p1].AnswerImage != "") {

                Log.d("qsize", "" + qsize)

//                var imgwidth: Int = 100

                Picasso.get().load(dataList[p1].AnswerImage)
                    .transform(transform.getTransformation(p0.opone1))
//                    .resize(qsize, p0.opone1.height)
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
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var opone1: ImageView = itemView.findViewById(R.id.option_one1)
        var opone: ImageView = itemView.findViewById(R.id.option_one)
        var abcd: TextView = itemView.findViewById(R.id.option_abcd)
    }

}
