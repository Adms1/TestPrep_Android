package com.testprep.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.location.Criteria
import android.location.LocationManager
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.testprep.R
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

//        fun openVersionDialogCharge(context: Activity) {
//            android.app.AlertDialog.Builder(context)
//                .setTitle("Saral Pay Update")
//                .setIcon(context.resources.getDrawable(R.mipmap.saralpay_icon))
//                .setMessage("Please update to a new version of the app.")
//                .setPositiveButton("Ok") { dialog, which ->
//                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adms.saralpay"))
//                    context.startActivity(i)
//                }
//                .setNegativeButton("Cancel") { dialog, which ->
//                    AppConfiguration.CustomerDetail.clear()
//                    val iLogin = Intent(context, LoginActivity::class.java)
//                    iLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context.startActivity(iLogin)
//                    context.finish()
//                }
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show()
//        }

        fun openInvalidApiKeyDialog(context: Activity) {
            android.app.AlertDialog.Builder(context)
                .setTitle("Account Alert")
                .setIcon(context.resources.getDrawable(R.mipmap.logo))
                .setMessage("We are currently setting up your account. Please contact customer support at +91 75758 09733 if you have any questions.")
                .setNegativeButton("Cancel") { dialog, which ->
                    // do nothing
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

        fun getIMEI(context: Context): String {
            val mngr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return mngr.deviceId

        }

        fun isLocationEnabled(context: Context): Boolean {
            var locationMode = 0
            val locationProviders: String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)

                } catch (e: Settings.SettingNotFoundException) {
                    e.printStackTrace()
                    return false
                }

                return locationMode != Settings.Secure.LOCATION_MODE_OFF

            } else {
                locationProviders =
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
                return !TextUtils.isEmpty(locationProviders)
            }
        }

        fun getLocation(context: Context): DoubleArray {
            val locationArray = DoubleArray(2)
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            val provider = locationManager.getBestProvider(criteria, false)

            if (isLocationEnabled(context)) {
                try {
                    val location = locationManager.getLastKnownLocation(provider)
                    val latValue = location.latitude
                    val longValue = location.longitude
                    if (latValue != 0.0 && longValue != 0.0) {
                        locationArray[0] = latValue
                        locationArray[1] = longValue
                    }
                } catch (e: SecurityException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return locationArray
        }

    }
}
