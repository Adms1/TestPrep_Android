package com.testcraft.testcraft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.interfaces.ChapterListInterface
import com.testcraft.testcraft.models.GetChapterList
import com.testcraft.testcraft.utils.Utils

class SelectSubjectAdapter(
    var cotext: Context,
    var dataList: ArrayList<GetChapterList.GetChapterData>, var getchapter: ChapterListInterface
) :
    RecyclerView.Adapter<SelectSubjectAdapter.MyViewHolder>() {

    var raw_index = -1
    var str = ""
    var strID = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.dialog_filter_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (position == 0) {
            Utils.setFont(cotext, "fonts/Inter-Bold.ttf", holder.textView)
        } else {
            Utils.setFont(cotext, "fonts/Inter-Regular.ttf", holder.textView)
        }

        holder.textView.text = dataList[position].Name

        holder.mainll.setOnClickListener {

            if (position == 0) {

                var newArr: ArrayList<GetChapterList.GetChapterData> = ArrayList()

                if (!dataList[0].isSelected) {
                    for (i in 0 until dataList.size) {
                        dataList[i].isSelected = true
                    }

                } else {
                    for (i in 0 until dataList.size) {
                        dataList[i].isSelected = false
                    }

                }

                for (i in 1 until dataList.size) {
                    newArr.add(dataList[i])
                }

                getchapter.getSelectedChapter(newArr)
                notifyDataSetChanged()

            } else {
                str = ""

                dataList[position].isSelected = !dataList[position].isSelected

                getchapter.getSelectedChapter(dataList)

                notifyDataSetChanged()
            }
        }

        if (dataList[position].isSelected) {

            holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.white_right_icn))
            holder.img.background = cotext.resources.getDrawable(R.drawable.login_btn_bg)

        } else {

            holder.img.setImageDrawable(cotext.resources.getDrawable(R.drawable.white_ring_bg))
            holder.img.background = cotext.resources.getDrawable(R.drawable.gray_ring_bg)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view)
        val mainll: RelativeLayout = view.findViewById(R.id.mainrl)
        var img: ImageView = view.findViewById(R.id.imgSelect)
    }

    fun sendArray(): ArrayList<GetChapterList.GetChapterData> {

        return dataList
    }

}
