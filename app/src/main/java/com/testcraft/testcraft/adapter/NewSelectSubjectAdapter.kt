package com.testcraft.testcraft.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testcraft.testcraft.R
import com.testcraft.testcraft.models.PackageData
import com.testcraft.testcraft.utils.ActionIdData
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.CommonWebCalls

class NewSelectSubjectAdapter(
    private val context: Context,
    employees: ArrayList<PackageData.PackageDataList>
) :
    RecyclerView.Adapter<NewSelectSubjectAdapter.MultiViewHolder>() {

    var all: ArrayList<PackageData.PackageDataList>? = null

    var selected1: ArrayList<Int> = ArrayList()

    val selected: ArrayList<PackageData.PackageDataList>
        get() {
            val selected: ArrayList<PackageData.PackageDataList> = ArrayList()
            for (i in 0 until all!!.size) {
                if (all!![i].isSelected) {
                    selected.add(all!![i])
                }
            }
            return selected
        }

    init {
        this.all = employees
    }

    fun setEmployees(employees: ArrayList<PackageData.PackageDataList>) {
        this.all = ArrayList()
        this.all = employees
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MultiViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.new_prefrence_item_layout, viewGroup, false)
        return MultiViewHolder(view)
    }

    override fun onBindViewHolder(multiViewHolder: MultiViewHolder, position: Int) {
        multiViewHolder.bind(all!![position])
    }

    override fun getItemCount(): Int {
        return all!!.size
    }

    inner class MultiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.package_image)
        var title: TextView = itemView.findViewById(R.id.package_name)
        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
        var p_select: ImageView = itemView.findViewById(R.id.package_select)

        fun bind(employee: PackageData.PackageDataList) {

            selected1 = ArrayList()

            getIds()

            if (employee.isSelected) {
                title.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
                p_select.visibility = View.VISIBLE
            } else {

                title.setTextColor(context.resources.getColor(R.color.black))
                p_select.visibility = View.GONE
            }

//            image.visibility = if (employee.isSelected) View.VISIBLE else View.GONE
            title.text = employee.SubjectName

            stitle.text = title.text.substring(0, 1)

            if (employee.Icon != "") {
                Picasso.get().load(AppConstants.IMAGE_BASE_URL + employee.Icon).into(image)
                stitle.visibility = View.GONE
            } else {
//            p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].SubjectName.substring(0, 1)))
                stitle.text = title.text.substring(0, 1)
            }

            itemView.setOnClickListener {

                CommonWebCalls.callToken(context, "1", "", ActionIdData.C703, ActionIdData.T703)

                employee.isSelected = !employee.isSelected

                if (employee.isSelected) {

                    if (employee.Icon == "") {
                        image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
                    }

                    title.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
                    p_select.visibility = View.VISIBLE
                } else {

                    if (employee.Icon == "") {
                        image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
                    }

                    title.setTextColor(context.resources.getColor(R.color.black))
                    p_select.visibility = View.GONE
                }

                notifyDataSetChanged()

            }

            for (i in 0 until all!!.size) {

                if (all!![i].isSelected) {
                    selected1.add(all!![i].SubjectID.toInt())
                }

                Log.i(
                    "selected array: ",
                    all!![i].SubjectName + "[" + all!![i].isSelected + "]"
                )
            }

        }
    }

    fun getIds(): String {

        return selected1.toString().replace("[", "").replace("]", "")

    }

}


//class NewSelectSubjectAdapter(val context: Context, val dataList: ArrayList<PackageData.PackageDataList>) :
//    RecyclerView.Adapter<NewSelectSubjectAdapter.viewholder>() {
//
//    var row_index = -1
//    var selected: ArrayList<Int> = ArrayList()
//
//    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewholder {
//
//        return viewholder(
//            LayoutInflater.from(context).inflate(
//                R.layout.new_prefrence_item_layout,
//                p0,
//                false
//            )
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return dataList.size
//    }
//
//    override fun onBindViewHolder(p0: viewholder, p1: Int) {
//
////        p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].SubjectName.substring(0, 1)))
//        p0.title.text = dataList[p1].SubjectName
////        p0.stitle.text = p0.title.text.substring(0, 1)
//
//        if (dataList[p1].Icon != null) {
//            Picasso.get().load(AppConstants.IMAGE_BASE_URL + dataList[p1].Icon).into(p0.image)
//        } else {
////            p0.image.setImageDrawable(Utils.newcreateDrawable(dataList[p1].SubjectName.substring(0, 1)))
//            p0.stitle.text = p0.title.text.substring(0, 1)
//        }
//
//        p0.image.setOnClickListener {
//
//            Utils.setStringValue(context, "subject_name", dataList[p1].SubjectName)
//
//            row_index = p1
//
//            notifyDataSetChanged()
//
//        }
//
//        if (dataList[p1].Icon == null) {
//            if (row_index == p1) {
//
//                p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))
//                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
//                p0.p_select.visibility = View.VISIBLE
//
//                if (selected.size > 0) {
//
//                    for (i in 0..selected.size) {
//                        if (selected.contains(dataList[p1].SubjectID.toInt())) {
//                            selected.remove(dataList[p1].SubjectID.toInt())
//                            p0.p_select.visibility = View.GONE
//                            p0.title.setTextColor(context.resources.getColor(R.color.black))
//                            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
//                            break
//
//                        } else {
//                            selected.add(dataList[p1].SubjectID.toInt())
//
//                            break
//
//                        }
//                    }
//
//                    Log.d("selected array : ", " $selected")
//
//                } else {
//                    selected.add(dataList[p1].SubjectID.toInt())
//                }
//
//            } else {
////            p0.title.setTextColor(context.resources.getColor(R.color.black))
////            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
//
//            }
//        } else {
//            if (row_index == p1) {
//
//                p0.title.setTextColor(context.resources.getColor(R.color.nfcolor))
////                p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.close_cancel))
//                p0.p_select.visibility = View.VISIBLE
//
//                if (selected.size > 0) {
//
//                    for (i in 0..selected.size) {
//                        if (selected.contains(dataList[p1].SubjectID.toInt())) {
//                            selected.remove(dataList[p1].SubjectID.toInt())
//                            p0.p_select.visibility = View.GONE
//                            p0.title.setTextColor(context.resources.getColor(R.color.black))
////                            p0.image.setImageDrawable(context.resources.getDrawable(R.drawable.gray_bg))
//                            break
//
//                        } else {
//                            selected.add(dataList[p1].SubjectID.toInt())
//
//                            break
//
//                        }
//                    }
//
//                    Log.d("selected array : ", " $selected")
//
//                } else {
//                    selected.add(dataList[p1].SubjectID.toInt())
//
//
//                    Log.d("selected array : ", " $selected")
//                }
//
//            } else {
//
//            }
//        }
//
//    }
//
//    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        var image: ImageView = itemView.findViewById(R.id.package_image)
//        var title: TextView = itemView.findViewById(R.id.package_name)
//        var stitle: TextView = itemView.findViewById(R.id.package_name_short)
//        var p_select: ImageView = itemView.findViewById(R.id.package_select)
//
//    }
//
//    fun getIds(): String {
//
//        val ids = selected.toString().replace("[", "").replace("]", "")
//
//        return ids
//
//    }
//
//
//}


