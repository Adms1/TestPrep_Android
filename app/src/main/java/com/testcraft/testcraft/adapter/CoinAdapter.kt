package com.testcraft.testcraft.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.testcraft.testcraft.R
import com.testcraft.testcraft.interfaces.CoinInteface
import com.testcraft.testcraft.models.CoinModel

@SuppressLint("SetTextI18n")
class CoinAdapter(
    val context: Context,
    val dataList: ArrayList<CoinModel>,
    var coinInteface: CoinInteface
) :
    RecyclerView.Adapter<CoinAdapter.viewholder>() {

    var row_index = 0

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(
                R.layout.purchase_coin_list_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        p0.coin.text = dataList[p1].coin
        p0.rupees.text = """Rs.${dataList[p1].rupees}"""

        p0.coin.setOnClickListener {

            row_index = p1
            notifyDataSetChanged()

            coinInteface.getCoin(dataList[p1].rupees)

        }

        if (row_index == p1) {
            p0.rupees.setTextColor(context.resources.getColor(R.color.nfcolor))
            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
        } else {
            p0.rupees.setTextColor(context.resources.getColor(R.color.black))
            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
        }

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var coin: TextView = itemView.findViewById(R.id.coin_item_short)
        var rupees: TextView = itemView.findViewById(R.id.coin_item_rupees)
        var image: ImageView = itemView.findViewById(R.id.coin_item_image)

    }
}
