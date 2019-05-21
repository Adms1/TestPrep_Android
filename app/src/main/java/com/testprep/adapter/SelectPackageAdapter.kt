package com.testprep.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.testprep.models.PackageData
import de.hdodenhof.circleimageview.CircleImageView

class SelectPackageAdapter(val context: Context, val dataList: ArrayList<PackageData>) :
    RecyclerView.Adapter<SelectPackageAdapter.viewholder>() {

    private val mDrawableBuilder: TextDrawable.IBuilder = TextDrawable.builder().round()
    private val mColorGenerator = ColorGenerator.MATERIAL

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {

        return viewholder(
            LayoutInflater.from(context).inflate(com.testprep.R.layout.list_item_select_package, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: viewholder, p1: Int) {

        if (dataList[p1].image != 0) {

            p0.image.visibility = GONE
            p0.image1.visibility = VISIBLE

            p0.image1.setImageResource(dataList[p1].imagee)

        } else {

            p0.image1.visibility = GONE
            p0.image.visibility = VISIBLE

//            createDrawable

            val drawable =
                mDrawableBuilder.build(dataList[p1].namee.substring(0, 1), mColorGenerator.getColor(dataList[p1]))
            p0.image.setImageDrawable(createDrawable(p1))
        }

        p0.itemView.setBackgroundColor(Color.TRANSPARENT)

        p0.name.text = dataList[p1].namee
        p0.price.text = dataList[p1].pricee

    }

    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(com.testprep.R.id.package_image)
        var image1: CircleImageView = itemView.findViewById(com.testprep.R.id.package_image1)
        var name: TextView = itemView.findViewById(com.testprep.R.id.package_name)
        var price: TextView = itemView.findViewById(com.testprep.R.id.package_price)

    }

    private fun createDrawable(pos: Int): Drawable {
        // Initialize a new GradientDrawable
        val gd = GradientDrawable()

//        val sf = object : ShapeDrawable.ShaderFactory() {
//            override fun resize(width: Int, height: Int): Shader {
//                return LinearGradient(
//                    0f, 0f, width.toFloat(), height.toFloat(),
//                    intArrayOf(Color.GREEN, Color.GREEN, Color.WHITE, Color.WHITE),
//                    floatArrayOf(0f, 0.5f, .55f, 1f), Shader.TileMode.REPEAT
//                )
//            }
//        }

        // Set the color array to draw gradient
        gd.colors = intArrayOf(mColorGenerator.getColor(dataList[pos]), Color.parseColor("#cdcdcd"))

        // Set the GradientDrawable gradient type linear gradient
        gd.gradientType = GradientDrawable.LINEAR_GRADIENT

        // Set GradientDrawable shape is a rectangle
        gd.shape = GradientDrawable.RECTANGLE

        gd.cornerRadius = 210F
        // Set 3 pixels width solid blue color border

        // Set GradientDrawable width and in pixels
        gd.setSize(390, 390) // Width 450 pixels and height 150 pixels

        // Set GradientDrawable as ImageView source image
        return gd

    }

}
