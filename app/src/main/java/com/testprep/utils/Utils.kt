package com.testprep.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testprep.old.models.QuestionResponse


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

        fun saveArrayList(context: Context, list: ArrayList<QuestionResponse.QuestionList>, key: String) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            val gson = Gson()
            val json = gson.toJson(list)
            editor.putString(key, json)
            editor.apply()     // This line is IMPORTANT !!!
        }

        fun getArrayList(context: Context, key: String): ArrayList<QuestionResponse.QuestionList> {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val gson = Gson()
            val json = prefs.getString(key, null)
            val type = object : TypeToken<ArrayList<QuestionResponse.QuestionList>>() {

            }.type
            return gson.fromJson(json, type)
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

        fun ping(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}
