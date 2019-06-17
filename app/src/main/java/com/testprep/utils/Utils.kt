package com.testprep.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.location.Criteria
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
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

        @SuppressLint("CommitPrefEdits")
        fun saveArrayList(context: Context, list: ArrayList<QuestionResponse.QuestionList>, key: String) {
            mSharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
            mSharedPreferencesEditor = mSharedPreferences!!.edit()
            val gson = Gson()
            val json = gson.toJson(list)
            mSharedPreferencesEditor!!.putString(key, json)
            mSharedPreferencesEditor!!.apply()     // This line is IMPORTANT !!!
        }

        fun getArrayList(context: Context, key: String): ArrayList<QuestionResponse.QuestionList> {
            mSharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = mSharedPreferences!!.getString(key, null)
            val type = object : TypeToken<ArrayList<QuestionResponse.QuestionList>>() {

            }.type
            return gson.fromJson(json, type)
        }

        fun clearPrefrence(context: Context) {
            mSharedPreferences = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE)
            mSharedPreferencesEditor = mSharedPreferences!!.edit()
            mSharedPreferencesEditor!!.clear()
            mSharedPreferencesEditor!!.commit()
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
                .setIcon(context.resources.getDrawable(com.testprep.R.mipmap.logo))
                .setMessage("We are currently setting up your account. Please contact customer support at +91 75758 09733 if you have any questions.")
                .setNegativeButton("Cancel") { dialog, which ->
                    // do nothing
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

//        fun getIMEI(context: Context): String {
//            val mngr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            return mngr.deviceId
//
//        }

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

        private val mColorGenerator = ColorGenerator.MATERIAL

        fun createDrawable(ch: Char, length: Int): Drawable {
            // Initialize a new GradientDrawable
            val gd = GradientDrawable()

            var pos = ch - 'a' + 1
            pos += length

            val lcolor = darker(pos, 0.1f)

//        val sf = object : ShapeDrawable.ShaderFactory() {
//            override fun resize(width: Int, height: Int): Shader {
//                return LinearGradient(
//                    0f, 0f, width.toFloat(), height.toFloat(),
//                    intArrayOf(Color.GREEN, Color.GREEN, Color.WHITE, Color.WHITE),
//                    floatArrayOf(0f, 0.5f, .55f, 1f), Shader.TileMode.REPEAT
//                )
//            }
//        }

            // Set the color array to draw gradient
            gd.colors = intArrayOf(lcolor, mColorGenerator.getColor(pos))

            // Set the GradientDrawable gradient type linear gradient
            gd.gradientType = GradientDrawable.LINEAR_GRADIENT

            // Set GradientDrawable shape is a rectangle
            gd.shape = GradientDrawable.RECTANGLE

            gd.cornerRadius = 210F
            // Set 3 pixels width solid blue color border

            // Set GradientDrawable width and in pixels
            gd.setSize(390, 390) // Width 450 pixels and height 150 pixels

            // Set GradientDrawable as ImageView source image
            return gd

        }

        fun darker(color: Int, factor: Float): Int {
            var red: Int = (Color.red(color) * factor).toInt()
            var green: Int = (Color.green(color) * factor).toInt()
            var blue: Int = (Color.blue(color) * factor).toInt()
            return argb(Color.alpha(color), red, green, blue)
        }

        fun newcreateDrawable(ch: String): Drawable {

            var color1 = R.color.dark_sky_blue

            val ic1 = TextDrawable.builder().buildRound(ch, color1)

            return ic1
        }

        fun changeStatusbarColor(context: Activity) {

            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )

//            val window = context.window
//
//// clear FLAG_TRANSLUCENT_STATUS flag:
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//
//// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//
//// finally change the color
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.statusBarColor = color
//            }
        }

    }
}
