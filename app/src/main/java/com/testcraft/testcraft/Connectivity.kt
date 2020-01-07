package com.testcraft.testcraft

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import com.testcraft.testcraft.utils.Utils


class Connectivity : BroadcastReceiver() {

    companion object {
        /**
         * Get the network info
         * @param context
         * @return
         */
        fun getNetworkInfo(context: Context): NetworkInfo? {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo
        }

        /**
         * Check if there is any connectivity
         * @param context
         * @return
         */
        fun isConnected(context: Context?): Boolean {
            val info: NetworkInfo = getNetworkInfo(context!!)!!
            return info != null && info.isConnected
        }

        /**
         * Check if there is any connectivity to a Wifi network
         * @param context
         * @param type
         * @return
         */
        fun isConnectedWifi(context: Context?): Boolean {
            val info: NetworkInfo = getNetworkInfo(context!!)!!
            return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
        }

        /**
         * Check if there is any connectivity to a mobile network
         * @param context
         * @param type
         * @return
         */
        fun isConnectedMobile(context: Context?): Boolean {
            val info: NetworkInfo = getNetworkInfo(context!!)!!
            return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
        }

        /**
         * Check if there is fast connectivity
         * @param context
         * @return
         */
        fun isConnectedFast(context: Context?): Boolean {

            if (isconnectedToWifi(context!!)) {

//                Utils.ping(context, "You have connected to Wi-Fi ")

                return true

            } else {

//                Utils.ping(context, "You have connected to Mobile Network ")

                val info: NetworkInfo = getNetworkInfo(context)!!

                return info != null && info.isConnected && isConnectionFast(
                    context,
                    info.type,
                    info.subtype
                )
            }
        }

        fun isconnectedToWifi(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val capabilities =
                    connectivityManager.getNetworkCapabilities(network)
                        ?: return false
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            } else {
                val networkInfo =
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        ?: return false
                networkInfo.isConnected
            }
        }

        /**
         * Check if the connection is fast
         * @param type
         * @param subType
         * @return
         */
        fun isConnectionFast(context: Context, type: Int, subType: Int): Boolean {
            return when (type) {
                ConnectivityManager.TYPE_WIFI -> {
//                    Utils.ping(context, "You have connected to Wi-Fi ")
                    true
                }

                ConnectivityManager.TYPE_MOBILE -> {
                    when (subType) {

                        TelephonyManager.NETWORK_TYPE_1xRTT -> {
                            Utils.ping(context, "The internet connection is very poor..")
                            false
                        }// ~ 50-100 kbps

                        TelephonyManager.NETWORK_TYPE_CDMA -> {
                            Utils.ping(context, "The internet connection is very poor..")
                            false
                        }// ~ 14-64 kbps

                        TelephonyManager.NETWORK_TYPE_EDGE -> {
                            Utils.ping(context, "The internet connection is very poor..")
                            false
                        }// ~ 50-100 kbps

                        TelephonyManager.NETWORK_TYPE_EVDO_0 -> {
                            Utils.ping(context, "The internet connection is very poor..")
                            true
                        }// ~ 400-1000 kbps

                        TelephonyManager.NETWORK_TYPE_EVDO_A -> {
                            Utils.ping(context, "The internet connection is very poor..")
                            true
                        }// ~ 600-1400 kbps

                        TelephonyManager.NETWORK_TYPE_GPRS -> {
                            Utils.ping(context, "The internet connection is very poor..")
                            false
                        }// ~ 100 kbps

                        TelephonyManager.NETWORK_TYPE_HSDPA -> {
//                            Utils.ping(context, "The internet connection is very good..")
                            true
                        }// ~ 2-14 Mbps

                        TelephonyManager.NETWORK_TYPE_HSPA -> {
                            Utils.ping(context, "The internet connection is slow.. ")
                            true
                        }// ~ 700-1700 kbps

                        TelephonyManager.NETWORK_TYPE_HSUPA -> {
                            Utils.ping(context, "The internet connection is slow.. ")
                            true
                        }// ~ 1-23 Mbps

                        TelephonyManager.NETWORK_TYPE_UMTS -> {
                            Utils.ping(context, "The internet connection is slow.. ")
                            true
                        }// ~ 400-7000 kbps

                        TelephonyManager.NETWORK_TYPE_EHRPD -> {
                            Utils.ping(context, "The internet connection is slow.. ")
                            true
                        }// ~ 1-2 Mbps

                        TelephonyManager.NETWORK_TYPE_EVDO_B -> {
//                            Utils.ping(context, "The internet connection is very good..")
                            true
                        }// ~ 5 Mbps

                        TelephonyManager.NETWORK_TYPE_HSPAP -> {
//                            Utils.ping(context, "The internet connection is very good..")
                            true
                        }// ~ 10-20 Mbps

                        TelephonyManager.NETWORK_TYPE_IDEN -> {
                            Utils.ping(context, "The internet connection is very poor..")
                            false
                        }// ~25 kbps

                        TelephonyManager.NETWORK_TYPE_LTE -> {
//                            Utils.ping(context, "The internet connection is very good..")
                            true
                        }// ~ 10+ Mbps

                        TelephonyManager.NETWORK_TYPE_UNKNOWN -> {
                            Utils.ping(context, "The internet connection is very poor..")
                            false
                        }
                        else -> {
                            Utils.ping(context, "The internet connection is very poor..")
                            false
                        }
                    }
                }
                else -> {
                    false
                }
            }
        }
    }


    override fun onReceive(context: Context?, intent: Intent?) {

        val connectivityManager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo

        if (activeNetwork != null && activeNetwork.isConnected) {
            isConnectedFast(context)
        } else {
            Utils.ping(context, "Network not reachable")
        }

//        val isConnected: Boolean =
//            intent!!.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
//        if (isConnected) {
//            Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show()
//        }


    }
}
