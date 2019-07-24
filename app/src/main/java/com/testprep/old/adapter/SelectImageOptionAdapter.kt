package com.testprep.old.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import com.squareup.picasso.Picasso
import com.testprep.R
import com.testprep.activity.TabwiseQuestionActivity
import com.testprep.old.models.QuestionResponse

class SelectImageOptionAdapter(
    val context: Context,
    val dataList: ArrayList<QuestionResponse.QuestionDataList>,
    var qsize: Int,
    var qid: String
) :

    RecyclerView.Adapter<SelectImageOptionAdapter.viewholder>() {

    private var lastCheckedRadioGroup: RadioGroup? = null
    private val lastCheckedPosition = -1

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

        var widhtx = 0
        var heightx = 0

//        Thread(Runnable {
//            try {
//                var url = URL("http://content.testcraft.co.in/question/" + dataList[p1].titleimg)
//                var bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//
//                widhtx = bmp.width
//                heightx = bmp.height
//
//                Log.d("imgsizeee", "widht$widhtx  height$heightx")
//
//                // Your implementation
//            } catch (ex: Exception) {
//                ex.printStackTrace()
//            }
//        }).start()

        if ("http://content.testcraft.co.in/question/" + dataList[p1].AnswerImage != "") {

            Log.d("qsize", "" + qsize)

            var imgwidth: Int = 100

            Picasso.get().load("http://content.testcraft.co.in/question/" + dataList[p1].AnswerImage)
                .resize(qsize, p0.opone1.height)
//                .fit()
//                .centerInside()
                .into(p0.opone1)
        }

        var id = (p1 + 1) * 100
//        for (i in 0..4) {
        val rb = RadioButton(this@SelectImageOptionAdapter.context)

        rb.layoutParams = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.WRAP_CONTENT
        )

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

            if (dataList[p1].IsCorrectAnswer) {
                TabwiseQuestionActivity.setButton(dataList[p1].MultipleChoiceQuestionAnswerID, qid, true)
            } else {
                TabwiseQuestionActivity.setButton(dataList[p1].MultipleChoiceQuestionAnswerID, qid, false)
            }
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

//                p0.llmain.setBackgroundResource(R.drawable.gray_ring_bg)

            } else if (lastCheckedRadioGroup == null) {
//                p0.llmain.setBackgroundResource(R.drawable.gray_ring_bg)

            } else {
//                p0.llmain.setBackgroundResource(R.drawable.white_ring_bg)

            }
            lastCheckedRadioGroup = p0.opone
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var opone1: ImageView = itemView.findViewById(R.id.option_one1)
        var opone: RadioGroup = itemView.findViewById(R.id.option_one)
        var llmain: RelativeLayout = itemView.findViewById(R.id.option_lll)
    }

//    internal inner class RetrieveFeedTask : AsyncTask<String, Void, Bitmap>() {
//
//        private var exception: Exception? = null
//
//
//
//        override fun doInBackground(urls: String): Bitmap? {
//            try {
//                var url = URL(urls)
//                var bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//
//                var widhtx = bmp.width
//                var heightx = bmp.height
//
//                return bmp
//            } catch (e: Exception) {
//                this.exception = e
//
//                return null
//            } finally {
////                `is`.close()
//            }
//        }
//
//        override fun onPostExecute(feed: Bitmap) {
//            // TODO: check this.exception
//            // TODO: do something with the feed
//
//
//        }
//    }

}
