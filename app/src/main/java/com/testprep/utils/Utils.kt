package com.testprep.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager

class Utils {

    companion object {

        private const val APP_PREF = "TesetPrep"
        private var mSharedPreferencesEditor: SharedPreferences.Editor? = null
        private var mSharedPreferences: SharedPreferences? = null

        @SuppressLint("CommitPrefEdits")
        fun setStringValue(context: Context, key: String, value: String) {
            mSharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
            mSharedPreferencesEditor = mSharedPreferences!!.edit()
            mSharedPreferencesEditor!!.putString(key, value)
            mSharedPreferencesEditor!!.apply()
        }

        fun getStringValue(context: Context, key: String, defaultValue: String): String? {
            mSharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
            return mSharedPreferences!!.getString(key, defaultValue)
        }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
