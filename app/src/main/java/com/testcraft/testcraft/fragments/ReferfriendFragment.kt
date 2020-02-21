package com.testcraft.testcraft.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testcraft.testcraft.R
import kotlinx.android.synthetic.main.fragment_referfriend.*


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
                "https://play.google.com/store/apps/details?id=com.testcraft.testcraft&hl=en & Code = " + referfrd_etCode.text
            )
            startActivity(Intent.createChooser(share, "Share Text"))
        }

    }

}
