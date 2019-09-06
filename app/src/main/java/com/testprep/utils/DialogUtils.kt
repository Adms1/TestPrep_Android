package com.testprep.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.support.v7.app.AlertDialog
import android.view.Window


class DialogUtils {

    companion object {

        var dialog: Dialog? = null

        fun isNetworkConnected(context: Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val allNetworks = manager?.allNetworks?.let { it } ?: return false
                allNetworks.forEach { network ->
                    val info = manager.getNetworkInfo(network)
                    if (info.state == NetworkInfo.State.CONNECTED) return true
                }
            } else {
                val allNetworkInfo = manager?.allNetworkInfo?.let { it } ?: return false
                allNetworkInfo.forEach { info ->
                    if (info.state == NetworkInfo.State.CONNECTED) return true
                }
            }
            return false
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

//            builder.setView(view)
            builder.setPositiveButton(positiveBtnTxt, positiveClickListener)
            builder.setNegativeButton(nagativeBtnTxt, negativeClickListener)

            return builder.create()

        }

        fun showDialog(context: Context) {
            if (dialog == null) {
                dialog = Dialog(context)
                dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog!!.setContentView(com.testprep.R.layout.progressbar_dialog)
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
            context: Activity,
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

    }

}
