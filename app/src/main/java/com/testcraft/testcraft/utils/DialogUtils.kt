package com.testcraft.testcraft.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk.getApplicationContext
import com.testcraft.testcraft.R

class DialogUtils {

    companion object {

        var dialog: Dialog? = null
        var netdialog: Dialog? = null

//        fun isNetworkConnected(context: Context): Boolean {
//            val manager =
//                context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                val allNetworks = manager?.allNetworks?.let { it } ?: return false
//                allNetworks.forEach { network ->
//                    val info = manager.getNetworkInfo(network)
//                    if (info.state == NetworkInfo.State.CONNECTED) return true
//                }
//            } else {
//                val allNetworkInfo = manager?.allNetworkInfo?.let { it } ?: return false
//                allNetworkInfo.forEach { info ->
//                    if (info.state == NetworkInfo.State.CONNECTED) return true
//                }
//            }
//            return false
//        }

        fun isNetworkConnected(getApplicationContext: Context): Boolean {
            var status = false
            val cm =
                getApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null) {
                    // connected to the internet
                    status = true
                }
            } else {
                if (cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting) {
                    // connected to the internet
                    status = true
                }
            }
            return status
        }

        fun createConfirmDialog(
            context: Context, titleId: String, messageId: String,
            positiveBtnTxt: String,
            nagativeBtnTxt: String,
//            view: View,
            positiveClickListener: DialogInterface.OnClickListener,
            negativeClickListener: DialogInterface.OnClickListener
        ): Dialog {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(titleId)
            builder.setMessage(messageId)
            builder.setCancelable(false)

//            builder.setView(view)
            builder.setPositiveButton(positiveBtnTxt, positiveClickListener)
            builder.setNegativeButton(nagativeBtnTxt, negativeClickListener)

            val dialog: AlertDialog = builder.create()
            dialog.window!!.setBackgroundDrawableResource(R.drawable.white_ring_bg)

            return dialog

        }

        fun showDialog(context: Context) {

            if (dialog == null) {
                dialog = Dialog(context)
                dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog!!.setContentView(R.layout.progressbar_dialog)

                val imgpro: ImageView = dialog!!.findViewById(R.id.imgprogress)
                Glide.with(getApplicationContext()).load(R.drawable.progress_gif).into(imgpro)

                dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog!!.setCanceledOnTouchOutside(false)
                dialog!!.setCancelable(false)
                dialog!!.show()
            }

        }

        fun dismissDialog() {
            if (dialog != null) {
                dialog!!.dismiss()
                dialog = null
            }
        }

        fun createConfirmDialog1(
            context: Context,
            positiveBtnTxt: String,
            titleId: String,
            positiveClickListener: DialogInterface.OnClickListener
        ): Dialog {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(titleId)
            //        builder.setMessage(messageId);
            // builder.setView(view);
            builder.setPositiveButton(positiveBtnTxt, positiveClickListener)

            builder.setCancelable(false)

            return builder.create()
        }

        fun NetworkDialog(context: Context) {

//            if(netdialog == null) {
            netdialog = Dialog(context)
            netdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            netdialog!!.setContentView(R.layout.dialog_network)
            netdialog!!.setCanceledOnTouchOutside(false)

            val btnRetry: TextView = netdialog!!.findViewById(R.id.network_btnRetry)

            btnRetry.setOnClickListener {
                if (isNetworkConnected(context)) {
                    netdialog!!.dismiss()
                }
            }

            netdialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            netdialog!!.setCanceledOnTouchOutside(false)
            netdialog!!.setCancelable(false)
            netdialog!!.show()
//            }else {
//                netdialog!!.dismiss()
//            }
        }

    }
}
