package com.testprep.utils

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AlertDialog

class DialogUtils {

    companion object {

//        fun createConfirmDialog(
//            context: Context, @StringRes titleId: Int, @StringRes messageId: Int,
//            view: View,
//            positiveClickListener: DialogInterface.OnClickListener,
//            negativeClickListener: DialogInterface.OnClickListener
//        ): Dialog {
//            val builder = AlertDialog.Builder(context)
//            builder.setTitle(titleId)
//            builder.setMessage(messageId)
//            builder.setView(view)
//            builder.setPositiveButton(R.string.ok, positiveClickListener)
//            builder.setNegativeButton(R.string.mdtp_cancel, negativeClickListener)
//
//            return builder.create()
//        }


        fun createConfirmDialog(
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

            return builder.create()
        }

    }

}
