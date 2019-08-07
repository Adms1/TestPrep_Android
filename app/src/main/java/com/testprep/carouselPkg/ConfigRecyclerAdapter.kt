//package com.e.carouseldemo
//
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CompoundButton
//import android.widget.SeekBar
//import android.widget.Switch
//import android.widget.TextView
//
//import com.e.carouseldemo.CarouselParameters.Companion
//
//import java.lang.reflect.Method
//import java.util.ArrayList
//import java.util.HashMap
//
//import butterknife.Bind
//import butterknife.ButterKnife
//import com.testprep.R
//import com.testprep.carouselPkg.CarouselView1
//
///**
// * @author  sunny-chung
// */
//
//class ConfigRecyclerAdapter(aClass: Class<CarouselView1.ViewTransformer>) : RecyclerView.Adapter<ConfigRecyclerAdapter.ViewHolder>() {
//
//    internal var mClass: Class<*>? = null
//    internal var mSetterMethods: List<Method> = ArrayList()
//    internal val mLock = Any()
//
//    internal var mValues: MutableMap<String, Number> = HashMap() // key = beanName; value = actual value (boolean stores as Byte 1/0)
//
//    val selectedValues: Map<String, Number>
//        get() = mValues
//
//    init {
//        setClass(aClass)
//    }
//
//    fun setClass(aClass: Class<CarouselView1.ViewTransformer>) {
//        mClass = aClass
//
//        synchronized(mLock) {
//            if (mClass != null) {
//                mSetterMethods = CarouselParameters.getSetterMethods(aClass)
//                mValues = CarouselParameters.getDefaultTransformerParameters<CarouselView1.ViewTransformer>(aClass, mSetterMethods)
//            } else {
//                mSetterMethods = ArrayList()
//                mValues = HashMap()
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.config_item, parent, false))
//    }
//
//    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
//        val method: Method
//        synchronized(mLock) {
//            method = mSetterMethods[position]
//        }
//
//        val beanName = CarouselParameters.getBeanName(method.name)
//
//        /**
//         * if cell changed, reconfigure the whole cell;
//         * otherwise, just update the result display text
//         */
//        val isCellChanged = vh.oldPosition != vh.position
//
//        if (isCellChanged) {
//
//            val parameterName = CarouselParameters.getParameterName(method.name)
//            vh.lblTitle!!.text = parameterName
//
//            vh.sekParameter!!.visibility = View.GONE
//            vh.swhParameter!!.visibility = View.GONE
//
//        }
//
//        val parameterType = method.parameterTypes[0]
//        if (Float::class.java == parameterType
//                || Float::class.javaPrimitiveType == parameterType
//                || Double::class.java == parameterType
//                || Double::class.javaPrimitiveType == parameterType) {
//
//            vh.sekParameter!!.visibility = View.VISIBLE
//
//            val info = CarouselParameters.getParameterRangeInfoDouble(method)
//            vh.sekParameter!!.max = info.numIntervals + 1
//
//            if (isCellChanged) {
//
//                val lastValue = mValues[beanName]
//
//                var chosenPosition = 0
//                if (lastValue != null) {
//                    chosenPosition = Math.round((lastValue.toDouble() - info.from) / info.interval + 1).toInt()
//                }
//                vh.sekParameter!!.progress = chosenPosition
//
//                vh.sekParameter!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                        if (fromUser) {
//                            if (progress != 0) {
//                                val value = info.from + (vh.sekParameter!!.progress - 1) * info.interval
//                                mValues[beanName] = value
//                            } else {
//                                mValues.remove(beanName)
//                            }
//                            notifyItemChanged(position)
//                        }
//                    }
//
//                    override fun onStartTrackingTouch(seekBar: SeekBar) {
//
//                    }
//
//                    override fun onStopTrackingTouch(seekBar: SeekBar) {
//
//                    }
//                })
//
//            }
//
//            val value = mValues[beanName]
//            if (value == null) {
//                vh.lblResult!!.text = "OFF"
//            } else {
//                vh.lblResult!!.text = String.format("%.1f", value.toDouble())
//            }
//
//        } else if (Int::class.java == parameterType || Int::class.javaPrimitiveType == parameterType) {
//
//            vh.sekParameter!!.visibility = View.VISIBLE
//
//            val info = CarouselParameters.getParameterRangeInfoInt(method)
//            vh.sekParameter!!.max = info.numIntervals + 1
//
//            if (isCellChanged) {
//
//                val lastValue = mValues[beanName]
//
//                var chosenPosition = 0
//                if (lastValue != null) {
//                    chosenPosition = Math.round((lastValue.toDouble() - info.from) / info.interval + 1).toInt()
//                }
//                vh.sekParameter!!.progress = chosenPosition
//
//                vh.sekParameter!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                        if (fromUser) notifyItemChanged(position)
//                    }
//
//                    override fun onStartTrackingTouch(seekBar: SeekBar) {
//
//                    }
//
//                    override fun onStopTrackingTouch(seekBar: SeekBar) {
//
//                    }
//                })
//
//            }
//
//            when (vh.sekParameter!!.progress) {
//                0 -> {
//                    vh.lblResult!!.text = "OFF"
//                }
//
//                else -> {
//                    val value = info.from + (vh.sekParameter!!.progress - 1) * info.interval
//                    vh.lblResult!!.text = "" + value
//                }
//            }
//
//        } else if (Boolean::class.java == parameterType || Boolean::class.javaPrimitiveType == parameterType) {
//
//            vh.swhParameter!!.visibility = View.VISIBLE
//
//            if (isCellChanged) {
//
//                vh.swhParameter!!.setOnCheckedChangeListener(null)
//
//                val lastValue = mValues[beanName]
//                val newChecked = lastValue != null && lastValue.toByte().toInt() != 0
//                vh.swhParameter!!.isChecked = newChecked
//
//                vh.swhParameter!!.setOnCheckedChangeListener { buttonView, isChecked ->
//                    mValues[beanName] = if (isChecked) 1.toByte() else 0.toByte()
//                    notifyItemChanged(position)
//                }
//
//            }
//
//            vh.lblResult!!.text = if (vh.swhParameter!!.isChecked) "ON" else "OFF"
//
//        } else {
//
//            vh.lblResult!!.text = "Unsupported Type - $parameterType"
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        synchronized(mLock) {
//            return mSetterMethods.size
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        @Bind(R.id.lblTitle)
//        internal var lblTitle: TextView? = null
//        @Bind(R.id.sekParameter)
//        internal var sekParameter: SeekBar? = null
//        @Bind(R.id.swhParameter)
//        internal var swhParameter: Switch? = null
//        @Bind(R.id.lblResult)
//        internal var lblResult: TextView? = null
//
//        init {
//            ButterKnife.bind(this, itemView)
//        }
//    }
//}
