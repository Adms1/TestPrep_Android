package com.testcraft.testcraft.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.testcraft.testcraft.R
import com.testcraft.testcraft.retrofit.WebClient
import com.testcraft.testcraft.retrofit.WebInterface
import com.testcraft.testcraft.utils.AppConstants
import com.testcraft.testcraft.utils.Utils
import kotlinx.android.synthetic.main.fragment_referfriend.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class ReferfriendFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_referfriend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        referfrd_btnShare.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(
                Intent.EXTRA_TEXT,
                "Join me on TestCraft, a Test preparation platform.  You get to take a FREE TEST by using my Code : " + referfrd_etCode.text + ".\n" +
                        "Click on below link:\n" +
                        "IOS: itms-apps://itunes.apple.com/app/id1491298929?ls=1\n" +
                        "Android: https://play.google.com/store/apps/details?id=com.testcraft.testcraft&hl=en "
            )
            startActivity(Intent.createChooser(share, "Share Text"))
        }

        callRCode()

    }

    fun callRCode() {

        val apiService = WebClient.getClient().create(WebInterface::class.java)

        val call =
            apiService.getRefrenceCode(Utils.getStringValue(activity!!, AppConstants.USER_ID, "0")!!)

        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.body()!!.get("Status").asString == "true") {

                    referfrd_etCode.setText(response.body()!!.get("data").asString)

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Log error here since request failed
                Log.e("", t.toString())
            }
        })
    }

}
