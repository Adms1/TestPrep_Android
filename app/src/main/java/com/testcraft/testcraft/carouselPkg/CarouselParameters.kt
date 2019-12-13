package com.testcraft.testcraft.carouselPkg

import android.support.annotation.FloatRange
import android.support.annotation.IntRange
import android.util.Log
import android.view.Gravity
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

/**
 * @author  sunny-chung
 */

class CarouselParameters {

    companion object {

        val TRANSFORMER_CLASSES: List<Class<out CarouselView1.ViewTransformer>> = Arrays.asList(
            LinearViewTransformer::class.java,
            FlatMerryGoRoundTransformer::class.java
        )

        private val PARAMETER_NAMES = object : HashMap<String, String>() {
            init {
                put("rotateDegree", "Rotation Angle (Degree)")
                put("numPies", "Number of Pies")
                put("farAlpha", "Alpha at Distant Point")
                put("farScale", "Scale at Distant Point")
            }
        }

        private val PARAMETER_RANGES = object : HashMap<String, RangeInfo<Number>>() {
            init {
                // only "from" and "to" fields of RangeInfo are used
                put("yProjection", RangeInfo<Number>(0.0, 90.0, 0, 0))
                put("numPies", RangeInfo(1, Integer.MAX_VALUE, 0, 0))
                put("horizontalViewPort", RangeInfo<Number>(0.0, 1.0, 0, 0))
                put("farAlpha", RangeInfo<Number>(-1.0, 1.0, 0, 0))
            }
        }

        val GRAVITY = Collections.unmodifiableMap(object : LinkedHashMap<String, Int>() {
            init {
                put("LEFT", Gravity.LEFT)
                put("RIGHT", Gravity.RIGHT)
                put("TOP", Gravity.TOP)
                put("BOTTOM", Gravity.BOTTOM)
                put("CENTER", Gravity.CENTER)
                put("CENTER_HORIZONTAL", Gravity.CENTER_HORIZONTAL)
                put("CENTER_VERTICAL", Gravity.CENTER_VERTICAL)
                put("LEFT|BOTTOM", Gravity.LEFT or Gravity.BOTTOM)
                put("LEFT|CENTER_VERTICAL", Gravity.LEFT or Gravity.CENTER_VERTICAL)
                put("RIGHT|BOTTOM", Gravity.RIGHT or Gravity.BOTTOM)
                put("RIGHT|CENTER_VERTICAL", Gravity.RIGHT or Gravity.CENTER_VERTICAL)
            }
        })

        private val FLOAT_PARAMETER_DEFAULT_MAX_VALUE = 1.5
        private val FLOAT_PARAMETER_DEFAULT_MIN_VALUE = -1.5

        private val INT_PARAMETER_DEFAULT_MAX_VALUE = 10
        private val INT_PARAMETER_DEFAULT_MIN_VALUE = -10

        val transformerNames: List<String>
            get() {
                val names = ArrayList<String>()
                for (clazz in TRANSFORMER_CLASSES) {
                    names.add(clazz.simpleName)
                }
                return names
            }

        fun getParameterName(setterMethodName: String): String {
            if (!setterMethodName.matches("set[A-Z].*".toRegex())) {
                throw IllegalArgumentException("not setter method")
            }
            val beanName = getBeanName(setterMethodName)
            var parameterName = PARAMETER_NAMES[beanName]
            if (parameterName == null) {
                // generate a default name
                val sb = StringBuilder()
                val methodNameSuffix = setterMethodName.substring(3)
                var i = 0
                var j = 0
                while (i < methodNameSuffix.length) {
                    while (j < methodNameSuffix.length && !isUpperCase(methodNameSuffix[j])) {
                        ++j
                    }
                    if (j > i) {
                        if (sb.length != 0) sb.append(' ')
                        sb.append(methodNameSuffix.substring(i, j))
                        i = j
                    }
                    ++j
                }
                parameterName = sb.toString()
                PARAMETER_NAMES[methodNameSuffix] = parameterName
            }
            return parameterName
        }

        fun getSetterMethods(clazz: Class<out CarouselView1.ViewTransformer>): List<Method> {
            val result = ArrayList<Method>()
            val setterMethodPattern = Pattern.compile("set[A-Z].*")
            for (method in clazz.methods) {
                if (setterMethodPattern.matcher(method.name).matches()) {
                    result.add(method)
                }
            }
            return result
        }

        fun getSetterMethodMap(clazz: Class<out CarouselView1.ViewTransformer>): Map<String, Method> {
            val result = HashMap<String, Method>()
            val setterMethodPattern = Pattern.compile("set[A-Z].*")
            for (method in clazz.methods) {
                if (setterMethodPattern.matcher(method.name).matches()) {
                    result[getBeanName(method.name)] = method
                }
            }
            return result
        }

        fun <T : CarouselView1.ViewTransformer> getDefaultTransformerParameters(
            clazz: Class<T>,
            setterMethods: List<Method>?
        ): HashMap<String, Number> {
            var setterMethods = setterMethods
            val results = HashMap<String, Number>()
            val transformer: T
            try {
                transformer = clazz.getDeclaredConstructor().newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException(e)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            }

            if (setterMethods == null) {
                setterMethods = getSetterMethods(clazz)
            }

            for (setterMethod in setterMethods) {
                val parameterType = setterMethod.parameterTypes[0]
                val getterMethodName: String
                if (parameterType == Boolean::class.java || parameterType == Boolean::class.javaPrimitiveType) {
                    getterMethodName = "is" + setterMethod.name.substring(3)
                } else {
                    getterMethodName = "get" + setterMethod.name.substring(3)
                }

                try {
                    val getterMethod = clazz.getMethod(getterMethodName)

                    val result = getterMethod.invoke(transformer)

                    if (result != null) {

                        val beanName = getBeanName(setterMethod.name)

                        if (parameterType == Boolean::class.java || parameterType == Boolean::class.javaPrimitiveType) {

                            results[beanName] = if (result as Boolean) 1.toByte() else 0.toByte()

                        } else if (parameterType == Float::class.java
                            || parameterType == Float::class.javaPrimitiveType
                            || parameterType == Double::class.java
                            || parameterType == Double::class.javaPrimitiveType
                        ) {

                            val castedResult = (result as? Number)?.toDouble() ?: result as Double

                            if (!java.lang.Double.isNaN(castedResult)) {

                                results[beanName] = castedResult

                            }

                        } else if (parameterType == Int::class.java || parameterType == Int::class.javaPrimitiveType) {

                            results[beanName] = result as Int

                        }

                    }
                } catch (e: NoSuchMethodException) {
                    Log.w(
                        "CarouselViewDemo",
                        "Cannot find getter method " + getterMethodName + " from " + clazz.name
                    )
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    throw RuntimeException(e)
                } catch (e: IllegalAccessException) {
                    throw RuntimeException(e)
                } catch (e: ClassCastException) {
                    throw e
                }

            }

            return results
        }

        fun <T : CarouselView1.ViewTransformer> createTransformer(
            clazz: Class<out CarouselView1.ViewTransformer>,
            parameters: HashMap<String, Number>?
        ): CarouselView1.ViewTransformer {
            val transformer: CarouselView1.ViewTransformer
            try {
                transformer = clazz.getDeclaredConstructor().newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException(e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException(e)
            } catch (e: NoSuchMethodException) {
                throw RuntimeException(e)
            }

            if (parameters != null) {
                val setterMethods = getSetterMethodMap(clazz)
                for ((beanName, paramValue) in parameters) {
                    val setterMethod = setterMethods[beanName]
                    try {
                        if (paramValue is Double) {
                            val setterParamType = setterMethod!!.parameterTypes[0]
                            if (setterParamType == Float::class.java || setterParamType == Float::class.javaPrimitiveType) {
                                setterMethod.invoke(transformer, paramValue.toFloat())
                            } else {
                                setterMethod.invoke(transformer, paramValue.toDouble())
                            }
                        } else if (paramValue is Int) {
                            setterMethod!!.invoke(transformer, paramValue.toInt())
                        } else if (paramValue is Byte) {
                            setterMethod!!.invoke(transformer, paramValue.toByte().toInt() != 0)
                        } else {
                            throw IllegalArgumentException("unknown parameter type " + paramValue.javaClass)
                        }
                    } catch (e: IllegalAccessException) {
                        throw RuntimeException(e)
                    } catch (e: InvocationTargetException) {
                        throw RuntimeException(e)
                    }

                }
            }

            return transformer
        }

        fun getBeanName(setterMethodName: String): String {
            return setterMethodName.substring(3, 4).toLowerCase() + setterMethodName.substring(4)
        }

        private fun isUpperCase(c: Char): Boolean {
            return 'A' <= c && c <= 'Z'
        }

        private fun <A : Annotation> findAnnotation(
            annotations: Array<Annotation>?,
            type: Class<A>
        ): A? {
            if (annotations == null) return null
            for (annotation: Annotation in annotations) {
                if (type == annotation.annotationClass) {
                    return annotation as A
                }
            }
            return null
        }

        class RangeInfo<T>(var from: T, var to: T, var interval: T, var numIntervals: Int)

        fun getParameterRangeInfoDouble(setterMethod: Method): RangeInfo<Double> {

            var upper = FLOAT_PARAMETER_DEFAULT_MAX_VALUE
            var lower = FLOAT_PARAMETER_DEFAULT_MIN_VALUE

            // should fail because FloatRange's retention policy is CLASS
            val annotatedRange =
                findAnnotation(setterMethod.parameterAnnotations[0], FloatRange::class.java)
            if (annotatedRange != null) {
                lower = annotatedRange.from
                upper = annotatedRange.to
                if (java.lang.Double.isInfinite(lower)) lower = FLOAT_PARAMETER_DEFAULT_MIN_VALUE
                if (java.lang.Double.isInfinite(upper)) upper = FLOAT_PARAMETER_DEFAULT_MAX_VALUE
            } else {
                val range = PARAMETER_RANGES[getBeanName(setterMethod.name)]
                if (range != null) {
                    lower = range.from as Double
                    upper = range.to as Double
                    if (java.lang.Double.isInfinite(lower)) lower =
                        FLOAT_PARAMETER_DEFAULT_MIN_VALUE
                    if (java.lang.Double.isInfinite(upper)) upper =
                        FLOAT_PARAMETER_DEFAULT_MAX_VALUE
                }
            }

            val diff = upper - lower
            val interval: Double
            if (diff <= 3.0) {
                interval = 0.1
            } else if (diff <= 6.0) {
                interval = 0.2
            } else if (diff <= 15.0) {
                interval = 0.5
            } else {
                interval = 1.0
            }

            if (annotatedRange != null) {
                if (!annotatedRange.fromInclusive) {
                    lower = annotatedRange.from + interval
                    if (java.lang.Double.isInfinite(lower)) lower =
                        FLOAT_PARAMETER_DEFAULT_MIN_VALUE
                }

                if (!annotatedRange.toInclusive) {
                    upper = annotatedRange.to - interval
                    if (java.lang.Double.isInfinite(upper)) upper =
                        FLOAT_PARAMETER_DEFAULT_MAX_VALUE
                }
            }

            val numIntervals = Math.round((upper - lower) / interval).toInt()

            return RangeInfo(lower, upper, interval, numIntervals)
        }

        fun getParameterRangeInfoInt(setterMethod: Method): RangeInfo<Int> {

            var upper = INT_PARAMETER_DEFAULT_MAX_VALUE
            var lower = INT_PARAMETER_DEFAULT_MIN_VALUE

            // should fail because IntRange's retention policy is CLASS
            val annotatedRange =
                findAnnotation(setterMethod.parameterAnnotations[0], IntRange::class.java)
            if (annotatedRange != null) {
                val from =
                    annotatedRange.from // + (annotatedRange.fromInclusive() ? 0 : FLOAT_PARAMETER_EXCLUSIVE_OFFSET);
                val to =
                    annotatedRange.to // - (annotatedRange.toInclusive() ? 0 : FLOAT_PARAMETER_EXCLUSIVE_OFFSET);
                lower =
                    if (from > Integer.MIN_VALUE) from.toInt() else INT_PARAMETER_DEFAULT_MIN_VALUE
                upper = if (to < Integer.MAX_VALUE) to.toInt() else INT_PARAMETER_DEFAULT_MAX_VALUE
            } else {
                val range: RangeInfo<Number> = PARAMETER_RANGES[getBeanName(setterMethod.name)]!!
                if (range != null) {
                    val from = range.from.toInt()
                    val to = range.to.toInt()

                    lower = if (from > Integer.MIN_VALUE) from else INT_PARAMETER_DEFAULT_MIN_VALUE
                    upper = if (to < Integer.MAX_VALUE) to else INT_PARAMETER_DEFAULT_MAX_VALUE
                }
            }

            val diff = upper - lower
            val interval: Int
            if (diff <= 30) {
                interval = 1
            } else if (diff <= 60) {
                interval = 2
            } else if (diff <= 150) {
                interval = 5
            } else {
                interval = 10
            }

            val numIntervals = Math.floor((upper.toDouble() - lower) / interval).toInt()

            return RangeInfo(lower, upper, interval, numIntervals)
        }
    }

}
